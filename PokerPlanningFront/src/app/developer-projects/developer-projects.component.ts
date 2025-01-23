import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-developer-projects',
  templateUrl: './developer-projects.component.html',
  styleUrls: ['./developer-projects.component.css']
})
export class DeveloperProjectsComponent implements OnInit {
  isDeveloper: boolean = true;
  projects: any;
  role: string | null = '';
  user: string | null = '';
  constructor(private router: Router, private http: HttpClient) { }

  ngOnInit(): void {
    this.role = localStorage.getItem('role');
    this.user = localStorage.getItem('user');
    console.log(this.role);
    if (this.role !== 'DEVELOPPER') {
      this.isDeveloper=false;
      // Rediriger l'utilisateur s'il n'est pas DEVELOPPER
      this.router.navigate(['/not-authorized']);
      return;
    }

    try {
      const token = localStorage.getItem('token');
      if (token) {
        console.log(token);
        const headers = new HttpHeaders({
          'Authorization': `Bearer ${token}`
        });
        if(this.user!=='')
        this.http.get(`http://localhost:8080/projects/dev/${this.user}`, { headers }).subscribe(
          (data) => {
            this.projects = data;
            console.log('Projects fetched successfully:', data);
          },
          (error) => {
            console.error('Error fetching projects:', error);
            if (error.status === 401) {
              console.error('Unauthorized - Invalid or expired token');
            } else {
              console.error('Error status:', error.status);
            }
          }
        );
      } else {
        console.error('No token found in localStorage');
      }
    } catch (error) {
      console.error('An error occurred:', error);
    }
  }
}
