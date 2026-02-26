import { Component } from '@angular/core';
import {AuthService} from '../../core/services/auth.service';
import {FormsModule} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    FormsModule,
    MatButton,
    MatFormField,
    MatInput,
    MatLabel,
    NgIf
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  username: string = '';
  email: string = '';
  password: string = '';
  loginError: string = '';

  constructor(private authService: AuthService) {
  }

  ngOnInit() {
  }

  onSubmit(): void {
    this.authService.register(this.username, this.email, this.password).subscribe({
      error: (error) => {
        this.loginError = 'Something went wrong while registering!';
        console.error('Sign-up failed:', error);
      }
    });
  }
}
