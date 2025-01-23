import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { HttpClientModule, provideHttpClient, withFetch } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { SignInComponent } from './sign-in/sign-in.component';
import { TemplatePOComponent } from './template-po/template-po.component';
import { TemplateAdminComponent } from './template-admin/template-admin.component';
import { DashboardLayoutComponent } from './dashboard-layout/dashboard-layout.component';
import { ProjectsComponent } from './projects/projects.component';
import { TasksComponent } from './tasks/tasks.component';
import { EstimationsComponent } from './estimations/estimations.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { CreateProjectComponent } from './projects/create-project/create-project.component';
import { UpdateProjectComponent } from './projects/update-project/update-project.component';
import { ViewProjectComponent } from './projects/view-project/view-project.component';
import { ViewTaskComponent } from './tasks/view-task/view-task.component';
import { UpdateTaskComponent } from './tasks/update-task/update-task.component';
import { CreateTaskComponent } from './tasks/create-task/create-task.component';
import { CommentsComponent } from './comments/comments.component';
import { ViewEstimationComponent } from './estimations/view-estimation/view-estimation.component';
import { DeveloperProjectsComponent } from './developer-projects/developer-projects.component';
import { EstimationServiceComponent } from './estimations/estimation-service/estimation-service.component';
import { DeveloperTasksComponent } from './developer-tasks/developer-tasks.component';
import { ViewDevProjectComponent } from './developer-projects/view-dev-project/view-dev-project.component';
import { RedditComponent } from './reddit/reddit.component';

@NgModule({
  declarations: [
    AppComponent,
    SignUpComponent,
    SignInComponent,
    TemplatePOComponent,
    TemplateAdminComponent,
    DashboardLayoutComponent,
    ProjectsComponent,
    TasksComponent,
    EstimationsComponent,
    DashboardComponent,
    CreateProjectComponent,
    UpdateProjectComponent,
    ViewProjectComponent,
    ViewTaskComponent,
    UpdateTaskComponent,
    CreateTaskComponent,
    CommentsComponent,
    ViewEstimationComponent,
    DeveloperProjectsComponent,
    EstimationServiceComponent,
    DeveloperTasksComponent,
    ViewDevProjectComponent,
    RedditComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [
    provideClientHydration(),
    provideHttpClient(withFetch())  // Ajoutez withFetch ici
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
