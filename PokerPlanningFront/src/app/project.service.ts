import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  readonly API_URL="http://localhost:8080"

  readonly ENDPOINT_PROJECTS="/projects"

  constructor(private httpClient: HttpClient) {

   }
   getProjects(){
    return this.httpClient.get(this.API_URL+this.ENDPOINT_PROJECTS)
  }
}
