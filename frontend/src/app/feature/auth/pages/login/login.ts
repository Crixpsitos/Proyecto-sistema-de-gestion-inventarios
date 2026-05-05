import { Component } from '@angular/core';
import { LoginForm } from '@/feature/auth/components/login-form/login-form';

@Component({
  selector: 'app-login',
  imports: [LoginForm],
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
  host: { class: 'block min-h-screen w-full' },
})
export class Login {}
