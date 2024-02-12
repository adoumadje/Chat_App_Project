import { Injectable } from '@angular/core';
import { User } from '../interfaces/user';
import { BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private user = new BehaviorSubject<User | null>(null)
  private baseUrl = environment.apiBaseUrl
  
  getUser = this.user.asObservable()

  constructor(private http:HttpClient) { }

  setUser(theUser:User | null) {
    this.user.next(theUser)
  }

  getAllOtherUsers(userId:number | undefined) {
    return this.http.get(`${this.baseUrl}/get-all-other-users?userId=${userId}`)
  }
}
