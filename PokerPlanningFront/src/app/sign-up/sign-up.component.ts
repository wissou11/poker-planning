import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent {
  UserArray: any[] = [];
  userName: string = "";
  email: string = "";
  password: string = "";
  post: string = "";
  agreedToTerms: boolean = false;
  errorMessage: string = ""; // Variable pour stocker le message d'erreur
  showPassword: boolean = false; // Variable pour gérer la visibilité du mot de passe

  constructor(private http: HttpClient, private router: Router) {}

  register(form: any) {
    if (!form.valid || !this.post || !this.agreedToTerms) {
      this.errorMessage = 'Please fill out the form correctly.';
      return;
    }

    let bodyData = {
      "username": this.userName,
      "email": this.email,
      "password": this.password,
      "posts": [this.post]
    };

    this.http.post("http://localhost:8080/api/auth/signup", bodyData, { responseType: 'text' }).subscribe(
      (resultData: any) => {
        console.log(resultData);
        alert("User Registered Successfully");
        this.resetForm();
        this.router.navigate(['/sign-in']);
      },
      (error) => {
        this.errorMessage = error.error; // Capture the error message from the backend
      }
    );
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  resetForm() {
    this.userName = '';
    this.email = '';
    this.password = '';
    this.post = '';
    this.agreedToTerms = false;
  }
}
