import { Component, OnInit } from '@angular/core';
import { WebSocketService } from './services/web-socket.service';
import { AuthService } from './services/auth.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'frontend';

  constructor(private webSocketService:WebSocketService,
    private authService:AuthService) { } 

  ngOnInit(): void {
    console.log('web socket url =', environment.webSocketUrl)
    console.log('api base url =', environment.apiBaseUrl)
    this.webSocketService.connect()
  }
}
