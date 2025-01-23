import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Project } from '../../projects/project.model';

@Component({
  selector: 'app-view-dev-project',
  templateUrl: './view-dev-project.component.html',
  styleUrl: './view-dev-project.component.css'
})
export class ViewDevProjectComponent implements OnInit{
  projectId: any;
    project$: Project | null = null;
    tasks: any;
    developers: any;

    constructor(private route: ActivatedRoute, private http: HttpClient) { }

    ngOnInit(): void {
      try {
        this.projectId = this.route.snapshot.paramMap.get('id');
        console.log(this.projectId);
        if (this.projectId) {
          if (typeof window !== 'undefined') {
            const token = localStorage.getItem('token');
            if (token) {
              const headers = new HttpHeaders({
                'Authorization': `Bearer ${token}`
              });
              let response1 = this.http.get<Project>(`http://localhost:8080/projects/${this.projectId}`, { headers });
              response1.subscribe(
                (data) => {
                  console.log(data);
                  this.project$ = data
                  if (this.project$!=null) {
                    console.log(this.project$.developers);
                    //const developerIds = this.project$.developers.map((dev: { id: string }) => dev.id);
                    const idsString = this.project$.developers.join(',');
                    let response3 = this.http.get(`http://localhost:8080/api/users/developers/by-ids/${idsString}`, { headers });
                    response3.subscribe(
                      (data) => { this.developers = data; console.log(this.developers); },
                      (error) => { console.error('Error fetching developers:', error); }
                    );
                  }

                },
                (error) => {
                  console.error('Error fetching project:', error);
                }
              );
            } else {
              console.error('No token found');
            }
          } else {
            console.error('localStorage is not available');
          }
        }
      } catch (error) {
        console.error('An error occurred:', error);
      }

      try {
        if (this.projectId) {
          if (typeof window !== 'undefined') {
            const token = localStorage.getItem('token');
            if (token) {
              const headers = new HttpHeaders({
                'Authorization': `Bearer ${token}`
              });
              let response2 = this.http.get(`http://localhost:8080/projects/${this.projectId}/tasks`, { headers });
              response2.subscribe(
                (data) => {this.tasks= data;
                   console.log(this.tasks)},
                (error) => {
                  console.error('Error fetching project tasks:', error);
                }
              );

            } else {
              console.error('No token found');
            }
          } else {
            console.error('localStorage is not available');
          }
        }
      } catch (error) {
        console.error('An error occurred:', error);
      }
    }
}
