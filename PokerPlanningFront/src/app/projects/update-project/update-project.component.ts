import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { FormBuilder, FormGroup, FormArray, FormControl, Validators } from '@angular/forms';
import { Project } from '../project.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-update-project',
  templateUrl: './update-project.component.html',
  styleUrls: ['./update-project.component.css']
})
export class UpdateProjectComponent implements OnInit {
  projectId: any;
  project$: any;
  enddate$: any;
  startdate$: any;

  projectForm: FormGroup;
  developers: any;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private fb: FormBuilder,
    private router: Router
  ) {
    this.projectForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      description: ['', [Validators.required]],
      startdate: ['', [Validators.required]],
      enddate: ['', [Validators.required]],
      developers: new FormArray([])
    });

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

            let response = this.http.get(`http://localhost:8080/projects/${this.projectId}`, { headers });
            response.subscribe(
              (data) => {
                this.project$ = data;
                console.log(data);
                const startDateString = this.project$.startdate;
                const endDateString = this.project$.enddate;
                const startDate = this.parseDate(startDateString);
                const endDate = this.parseDate(endDateString);
                if (!startDate || !endDate) {
                  console.error('Invalid date value received:', { startDateString, endDateString });
                  return;
                }
                this.startdate$ = startDate;
                this.enddate$ = endDate;
                this.projectForm.patchValue({
                  title: this.project$.title,
                  description: this.project$.description,
                  startdate: this.startdate$.toISOString().slice(0, 10),
                  enddate: this.enddate$.toISOString().slice(0, 10)
                });
              },
              (error: HttpErrorResponse) => {
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
      console.error('An error occurred while initializing the project:', error);
    }
  }

  ngOnInit(): void {
    this.addDeveloperCheckboxes();
  }

  private addDeveloperCheckboxes(): void {
    this.developers.forEach(() => this.developerFormArray.push(new FormControl(false)));
  }

  get developerFormArray(): any {
    return this.projectForm.get('developers') as FormArray;
  }

  submit(): void {
    const selectedDevelopersIds = this.projectForm.value.developers
      .map((checked: boolean, i: number) => checked ? this.developers[i].id : null)
      .filter((v: number | null) => v !== null) as number[];

    const startdate = this.formatDate(this.projectForm.value.startdate);
    const enddate = this.formatDate(this.projectForm.value.enddate);

    const project: Project = {
      ...this.projectForm.value,
      startdate: startdate,
      enddate: enddate,
      developers: selectedDevelopersIds
    };

    try {
      if (typeof window !== 'undefined') {
        const token = localStorage.getItem('token');
        if (token) {
          const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
          });

          this.http.put(`http://localhost:8080/projects/${this.projectId}`, project, { headers }).subscribe(
            response => {
              console.log('Project updated successfully', response);
              this.router.navigate(['/dashboard/projects']);
            },
            (error: HttpErrorResponse) => {
              console.error('Error updating project:', error);
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

      console.log('Form Value:', {
        ...this.projectForm.value,
        developers: selectedDevelopersIds
      });
    } catch (error) {
      console.error('An error occurred while submitting the project:', error);
    }
  }

  private formatDate(date: string): string {
    const d = new Date(date);
    return `${String(d.getDate()).padStart(2, '0')}-${String(d.getMonth() + 1).padStart(2, '0')}-${d.getFullYear()}`;
  }

  private parseDate(dateString: string): Date | null {
    const [day, month, year] = dateString.split('-');
    if (!day || !month || !year) {
      return null;
    }
    return new Date(`${year}-${month}-${day}`);
  }
}
