import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.css']
})
export class ProjectsComponent implements OnInit {
  projects: any;
  role: string | null = '';

  constructor(private http: HttpClient, private router: Router) { }

  ngOnInit() {
    this.role = localStorage.getItem('role');
    if (this.role !== 'PRODUCTOWNER') {
      // Rediriger l'utilisateur s'il n'est pas Product Owner
      this.router.navigate(['/not-authorized']);
      return;
    }

    try {
      const token = localStorage.getItem('token');
      if (token) {
        const headers = new HttpHeaders({
          'Authorization': `Bearer ${token}`
        });

        this.http.get("http://localhost:8080/projects", { headers }).subscribe(
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
