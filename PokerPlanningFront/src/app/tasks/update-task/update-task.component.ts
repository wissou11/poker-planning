// update-task.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Task } from '../task.model';

@Component({
  selector: 'app-update-task',
  templateUrl: './update-task.component.html',
  styleUrls: ['./update-task.component.css']
})
export class UpdateTaskComponent implements OnInit {
  taskForm: FormGroup;
  projectId: any;
  taskId: any;
  task: any;

  constructor(private route: ActivatedRoute,
    private http: HttpClient,
    private fb: FormBuilder,
    private router: Router) {
    this.taskForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      description: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(200)]],
      duree: ['', [Validators.required]],
      status: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    try {
      this.projectId = this.route.snapshot.paramMap.get('id');
      console.log(this.projectId);
      this.taskId = this.route.snapshot.paramMap.get('taskid');
      console.log(this.taskId);
      if (this.projectId && this.taskId) {
        if (typeof window !== 'undefined') {
          const token = localStorage.getItem('token');
          if (token) {
            const headers = new HttpHeaders({
              'Authorization': `Bearer ${token}`
            });
            let response = this.http.get(`http://localhost:8080/projects/${this.projectId}/tasks/${this.taskId}`, { headers });
            response.subscribe(
              (data) =>{ this.task = data;
                console.log(this.task);
                this.taskForm.patchValue({
                  title: this.task.title,
                  description: this.task.description,
                  duree: this.task.duree,
                  status: this.task.status
                });
              },
              (error) => {
                console.error('Error fetching task:', error);
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


  submit(): void {
    const task: Task = {
      ...this.taskForm.value
    };
    try {
      if (typeof window !== 'undefined') {
        const token = localStorage.getItem('token');
        if (token) {
          const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
          });

          this.http.put(`http://localhost:8080/projects/${this.projectId}/tasks/${this.taskId}`, task, { headers }).subscribe(
            response => {
              console.log('Task updated successfully', response);
              this.router.navigate([`/dashboard/projects/view/${this.projectId}/tasks/view/${this.taskId}`]);
            },
            (error: HttpErrorResponse) => {
              console.error('Error updating task:', error);
              if (error.status === 0) {
                console.error('Network error - make sure the backend server is running');
              } else {
                console.error(`Backend returned code ${error.status}, body was: ${error.error}`);
              }
            }
          );

        } else {
          console.error('No token found');
        }
      } else {
        console.error('localStorage is not available');
      }
    console.log('Form Value:', this.taskForm.value);
  } catch (error) {
    console.error('An error occurred while submitting the project:', error);
  }
  }
}
