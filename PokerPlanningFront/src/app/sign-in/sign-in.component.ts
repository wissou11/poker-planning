import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent {
  username: string = '';
  password: string = '';
  showPassword: boolean = false; // Add showPassword property
  errorMessage: string = ''; // Add an error message property

  constructor(private http: HttpClient, private router: Router) {}

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  onSubmit() {
    if (!this.username && !this.password) {
      this.errorMessage = 'Username and Password are required.';
      return;
    }

    if (!this.username) {
      this.errorMessage = 'Username is required.';
      return;
    }

    if (!this.password) {
      this.errorMessage = 'Password is required.';
      return;
    }

    const user = {
      username: this.username,
      password: this.password
    };

    this.http.post('http://localhost:8080/api/auth/signin', user).subscribe(
      (response: any) => {
        if (response.token) {
          console.log(response.token);
          // console.log('Received JWT:', response.token);
          localStorage.setItem('token', response.token);

          // Store the user role in localStorage
          localStorage.setItem('role', response.posts[0]); // Assuming only one role per user

          localStorage.setItem('user', response.id);
          localStorage.setItem('username', user.username);

          // Redirect based on roles
          if (response.posts.includes('PRODUCTOWNER')) {
            this.router.navigate(['/dashboard/productowner-dashboard']);
          } else if (response.posts.includes('DEVELOPPER')) {
            this.router.navigate(['/dashboard/developer-dashboard']);
          } else {
            this.router.navigate(['/']);
          }
        } else {
          // Set the error message
          this.errorMessage = response;
        }
      },
      (error) => {
        console.error('Error occurred:', error);
        this.errorMessage = 'Authentication failed';
      }
    );
  }
}
