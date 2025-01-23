import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, FormControl, Validators } from '@angular/forms';
import { Project } from '../project.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-project',
  templateUrl: './create-project.component.html',
  styleUrls: ['./create-project.component.css']
})
export class CreateProjectComponent implements OnInit {
  projectForm: FormGroup;
  developers : any;
  role: string | null = '';
  creator: string | null = '';

  constructor(private fb: FormBuilder, private http: HttpClient, private router: Router) {
    this.projectForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      description: ['', [Validators.required]],
      startdate: ['', [Validators.required]],
      enddate: ['', [Validators.required]],
      creator: ['', [Validators.required]],
      developers: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.role = localStorage.getItem('role');
    if (this.role !== 'PRODUCTOWNER') {
      // Rediriger l'utilisateur s'il n'est pas Product Owner
      this.router.navigate(['/not-authorized']);
      return;
    }
    this.getdevelopers();

  }

  private getdevelopers(): any {

    try {
      const token = localStorage.getItem('token');
      if (token) {
        const headers = new HttpHeaders({
          'Authorization': `Bearer ${token}`
        });

        this.http.get("http://localhost:8080/api/users/developers", { headers }).subscribe(
          (data) => {
            this.developers = data;
            console.log('Developers fetched successfully:', data);
            this.addDeveloperCheckboxes();
          },
          (error) => {
            console.error('Error fetching developers:', error);
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
  private addDeveloperCheckboxes(): void {
    this.developers.forEach(() => this.developerFormArray.push(new FormControl(false)));
  }

  get developerFormArray(): any {
    return this.projectForm.get('developers') as FormArray;
  }

  submit(): void {
    try {
      const selectedDevelopersIds = this.projectForm.value.developers
        .map((checked: boolean, i: number) => checked ? this.developers[i].id : null)
        .filter((v: number | null) => v !== null) as number[];

      const startDate = this.formatDate(this.projectForm.value.startdate);
      const endDate = this.formatDate(this.projectForm.value.enddate);

      this.creator=localStorage.getItem('username');

      const project: Project = {
        ...this.projectForm.value,
        startdate: startDate,
        enddate: endDate,
        developers: selectedDevelopersIds,
        creator: this.creator,
      };

      if (typeof window !== 'undefined') {
        const token = localStorage.getItem('token');
        if (token) {
          const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
          });
          this.http.post('http://localhost:8080/projects', project, { headers }).subscribe(
            response => {
              console.log('Project created successfully', response);
              this.router.navigate(['/dashboard/projects']);
            },
            error => {
              console.error('Error creating project', error);
            }
          );
        } else {
          console.error('No token found');
        }
      } else {
        console.error('localStorage is not available');
      }

      console.log('Form Value:', {
        ...this.projectForm.value,
        developers: selectedDevelopersIds
      });
    } catch (error) {
      console.error('An error occurred:', error);
    }
  }

  private formatDate(date: string): string {
    const d = new Date(date);
    return `${String(d.getDate()).padStart(2, '0')}-${String(d.getMonth() + 1).padStart(2, '0')}-${d.getFullYear()}`;
  }
}
