import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  baseUrl = environment.apiBaseUrl

  constructor(private http:HttpClient) { }

  getAllChatRoomMessages() {
    return this.http.get(`${this.baseUrl}/get-all-chatroom-messages`)
  }

  getUserPrivateMessages(userId:number) {
    return this.http.get(`${this.baseUrl}/get-user-private-messages?userId=${userId}`)
  }
}
