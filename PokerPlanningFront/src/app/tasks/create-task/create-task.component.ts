// create-task.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { Task } from '../task.model';

@Component({
  selector: 'app-create-task',
  templateUrl: './create-task.component.html',
  styleUrls: ['./create-task.component.css']
})
export class CreateTaskComponent implements OnInit {
  taskForm: FormGroup;
  projectId: any;

  constructor(private fb: FormBuilder, private route: ActivatedRoute, private http: HttpClient, private router: Router) {
    this.taskForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      description: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(200)]],
      projectid: this.projectId
    });
  }

  ngOnInit(): void {

  }

  submit(): void {
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
            const task: Task = {
              ...this.taskForm.value,
              projectid: this.projectId
            };
            this.http.post(`http://localhost:8080/projects/${this.projectId}/tasks`, task, { headers }).subscribe(
              response => {
                console.log('Task created successfully', response);
                this.router.navigate([`/dashboard/projects/view/${this.projectId}`]);
              },
              error => {
                console.error('Error creating task', error);
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
    console.log('Form Value:', this.taskForm.value);
  }
}
