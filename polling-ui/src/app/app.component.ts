import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { PollVotingComponent } from './poll-voting/poll-voting.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [PollVotingComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {}
