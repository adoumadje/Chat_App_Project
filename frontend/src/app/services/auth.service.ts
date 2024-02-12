import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegistrationModel } from '../interfaces/registration-model';
import { LoginModel } from '../interfaces/login-model';
import { ForgotModel } from '../interfaces/forgot-model';
import { PasswordResetModel } from '../interfaces/password-reset-model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseURL = environment.apiBaseUrl

  constructor(private http:HttpClient) { }

  registerUser(userDetails:RegistrationModel) {
    return this.http.post(
      `${this.baseURL}/register-user`, 
      userDetails, 
      { responseType: "text" }
    )
  }

  loginUser(userDetails:LoginModel) {
    return this.http.post(`${this.baseURL}/login-user`, userDetails)
  }

  forgotPassword(userDetails:ForgotModel) {
    return this.http.post(`${this.baseURL}/forgot-password`, userDetails)
  }

  resetPassword(userDetails:PasswordResetModel) {
    return this.http.put(
      `${this.baseURL}/reset-password`, 
      userDetails,
      { responseType: "text" }
    )
  }

  logout(token:string | undefined) {
    return this.http.get(
      `${this.baseURL}/logout-user?userToken=${token}`,
      { responseType: "text"}
    )
  }
}
