import { Component } from '@angular/core';
import {AuthService} from '../../core/services/auth.service';
import {FormsModule} from '@angular/forms';
import {MatFormField, MatLabel} from "@angular/material/select";
import {MatButton} from '@angular/material/button';
import {MatInput} from '@angular/material/input';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    MatLabel,
    MatFormField,
    MatButton,
    MatInput,
    NgIf
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  loginError: string = '';
  constructor(private authService: AuthService) {
  }

  ngOnInit(): void {
  }

  onSubmit(): void {
    this.loginError = '';
    this.authService.login(this.username, this.password).subscribe({
      error: (error) => {
        this.loginError = 'Invalid username or password. Please try again.';
        console.error('Sign-in failed:', error);
      }
    });
  }
}
