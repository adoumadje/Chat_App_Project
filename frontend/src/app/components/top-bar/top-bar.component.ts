import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { User } from 'src/app/interfaces/user';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';
import { WebSocketService } from 'src/app/services/web-socket.service';

@Component({
  selector: 'app-top-bar',
  templateUrl: './top-bar.component.html',
  styleUrls: ['./top-bar.component.css']
})
export class TopBarComponent implements OnInit {
  displayConfirmationDialog:boolean = false
  isLoggedIn:boolean = false
  user:User | null = null

  constructor(
    private userService:UserService,
    private authService:AuthService,
    private messageService: MessageService,
    private router:Router,
    private webSocketService:WebSocketService
    ) { }

  ngOnInit(): void {
    this.userService.getUser.subscribe(userData => {
      if(userData != null) {
        this.isLoggedIn = true
        this.user = {...userData}
      } else if(sessionStorage.getItem('user') != null) {
        this.isLoggedIn = true
        const data:string = sessionStorage.getItem('user')?? "{}"
        this.user = JSON.parse(data)
      }
      
    })
  }

  askConfirmation() {
    this.displayConfirmationDialog = true
  }

  cancel() {
    this.displayConfirmationDialog = false
  }

  logout() {
    this.authService.logout(this.user?.token).subscribe(
      response => {
        sessionStorage.clear()
        this.webSocketService.sendMessage('/app/public-message', JSON.stringify({
          senderId: this.user?.id,
          senderName: this.user?.fullname,
          senderInitial: this.user?.initial,
          receiverId: 0,
          message: `${this.user?.fullname} has left`,
          messageType: "LEAVE"
        }))
        this.user = null
        this.isLoggedIn = false
        this.userService.setUser(null)
        this.displayConfirmationDialog = false
        this.router.navigate(['login'])
      },
      error => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Something went wrong'})
      }
    )
  }
}
