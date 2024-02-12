import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { MessageModel } from 'src/app/interfaces/message-model';
import { User } from 'src/app/interfaces/user';
import { ChatService } from 'src/app/services/chat.service';
import { UserService } from 'src/app/services/user.service';
import { WebSocketService } from 'src/app/services/web-socket.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  user:User | null = null
  usersList:User[] = []
  numOfOnlineUsers = 0
  chats:Map<number, MessageModel[]> = new Map()
  messageForm:any
  currentChat:number = 0
  chatroomIsActive:boolean = true

  constructor(
    private userService:UserService,
    private messageService:MessageService,
    private webSocketService:WebSocketService,
    private chatService:ChatService,
    private fb:FormBuilder
    ) { }
  

  ngOnInit(): void {
    this.messageForm = this.fb.group({
      textMessage: ['', Validators.required]
    })


    this.userService.getUser.subscribe(userData => {
      if(userData != null) {
        this.user = userData
      } else if (sessionStorage.getItem('user') != null) {
        this.user = JSON.parse(sessionStorage.getItem('user') as string)
      }
    })


    this.chats.set(0, [])


    this.chatService.getAllChatRoomMessages().subscribe(
      response => {
        const existingArr:MessageModel[] = this.chats.get(0) as MessageModel[]
        existingArr.push(...response as MessageModel[])
      },
      error => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Something went wrong' })
      }
    )


    this.userService.getAllOtherUsers(this.user?.id).subscribe(
      response => {
        this.usersList = response as User[]
        let x = 0
        this.usersList.forEach(usr => {
          if(usr.status === "online") {
            x += 1
          }

          this.chats.set(usr.id, [])

        })
        this.numOfOnlineUsers = x + 1


        this.chatService.getUserPrivateMessages(this.user?.id as number).subscribe(
          response => {
            const privateMessages:MessageModel[] = response as MessageModel[]
            privateMessages.forEach(privateMessage => {
              if(privateMessage.senderId == this.user?.id) {
                const existingArr:MessageModel[] = this.chats.get(privateMessage.receiverId) as MessageModel[]
                existingArr.push(privateMessage)
              } else if(privateMessage.receiverId == this.user?.id) {
                const existingArr:MessageModel[] = this.chats.get(privateMessage.senderId) as MessageModel[]
                existingArr.push(privateMessage)
              }
            })
          },
          error => {
    
          }
        )

        
      },
      error => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Something went wrong'})
      }
    )



    setTimeout(() => {
      this.webSocketService.subscribe('/chatroom/public', (payload) => {
        const message = JSON.parse(payload.body) as MessageModel

        switch(message.messageType) {
          case "JOIN":
            this.usersList.forEach(usr => {
              if(usr.id === message.senderId && usr.status === "offline") {
                usr.status = "online"
                this.numOfOnlineUsers += 1
              }
            })
            break
          case "LEAVE":
            this.usersList.forEach(usr => {
              if(usr.id === message.senderId && usr.status === "online") {
                usr.status = "offline"
                this.numOfOnlineUsers -= 1
              }
            })
            break
          case "MESSAGE":
            const existingArr:MessageModel[] = this.chats.get(0) as MessageModel[]
            existingArr.push(message)
            break
          default:
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Something went wrong'})
            
        }
      })

      const privateDestination:string = '/user/' + this.user?.id + '/private'
      this.webSocketService.subscribe(privateDestination, (payload) => {
        const message = JSON.parse(payload.body) as MessageModel
        const existingArr:MessageModel[] = this.chats.get(message.senderId) as MessageModel[]
        existingArr.push(message)
      })


      this.webSocketService.sendMessage('/app/public-message', JSON.stringify({
        senderId: this.user?.id,
        senderName: this.user?.fullname,
        senderInitial: this.user?.initial,
        receiverId: 0,
        message: `${this.user?.fullname} has joined`,
        messageType: 'JOIN'
      }))

    }, 1000);
  }

  get textMessage() {
    return this.messageForm.controls['textMessage']
  }

  sendTextMessage() {
    if(this.currentChat === 0) {
      this.webSocketService.sendMessage('/app/public-message', JSON.stringify({
        senderId: this.user?.id,
        senderName: this.user?.fullname,
        senderInitial: this.user?.initial,
        receiverId: 0,
        message: this.textMessage.value,
        messageType: "MESSAGE"
      }))
    } else {
      const theMessage:MessageModel = {
        senderId: this.user?.id,
        senderName: this.user?.fullname,
        senderInitial: this.user?.initial,
        receiverId: this.currentChat,
        message: this.textMessage.value,
        messageType: "MESSAGE"
      } as MessageModel
      const existingArr:MessageModel[] = this.chats.get(this.currentChat) as MessageModel[]
      existingArr.push(theMessage)
      this.webSocketService.sendMessage('/app/private-message', JSON.stringify(theMessage))
    }
    this.messageForm.reset()
  }

  activateChatroom() {
    this.chatroomIsActive = true
    this.currentChat = 0
    this.usersList.forEach(usr => {
      usr.isActiveChat = false
    })
  }

  activatePrivateChat(inpId:number) {
    this.chatroomIsActive = false
    this.currentChat = inpId
    this.usersList.forEach(usr => {
      if(usr.id == inpId) {
        usr.isActiveChat = true
      } else {
        usr.isActiveChat = false
      }
    })
  }
}
