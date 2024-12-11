import {
  AfterViewInit,
  Component,
  Inject,
  NgZone,
  OnInit,
  PLATFORM_ID,
} from '@angular/core';
import { Poll, PollOptionPercentage } from '../models/Poll';
import { PollService } from '../service/poll-service/poll.service';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { LocalStorageService } from '../service/local-storage-service/local-storage.service';

@Component({
  selector: 'app-poll-voting',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './poll-voting.component.html',
  styleUrl: './poll-voting.component.css',
})
export class PollVotingComponent implements OnInit, AfterViewInit {
  voteForm: FormGroup;
  poll!: Poll;
  submitted: boolean = false;
  votePercentages: PollOptionPercentage = {};

  constructor(
    private fb: FormBuilder,
    private pollService: PollService,
    private localStorageService: LocalStorageService,
    private zone: NgZone,
    @Inject(PLATFORM_ID) private platformId: object
  ) {
    this.voteForm = this.createVoteForm();
  }

  ngOnInit(): void {
    this.loadActivePoll();
    this.listenForPollUpdates();
  }

  ngAfterViewInit(): void {
    this.loadUserVote();
  }

  private createVoteForm(): FormGroup {
    return this.fb.group({
      selectedOption: ['', Validators.required],
    });
  }

  private loadActivePoll(): void {
    this.pollService.getActivePoll().subscribe({
      next: (data: any) => {
        this.resetPoll();
        this.poll = data;
      },
      error: this.handleError('Error fetching active poll'),
    });
  }

  private listenForPollUpdates(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.pollService.listenForPollUpdates().subscribe({
        next: (newPoll) => {
          this.zone.run(() => {
            this.resetPoll();
            this.poll = newPoll;
          });
        },
        error: this.handleError('Error listening for poll updates'),
      });
    }
  }

  private loadUserVote(): void {
    if (!this.poll) return;

    const storedVote = this.localStorageService.getItem(
      this.poll.id.toString()
    );
    if (storedVote) {
      this.voteForm.patchValue({ selectedOption: storedVote });
      this.submitted = true;
      this.fetchPollResults();
    }
  }

  selectOption(option: string) {
    console.log(option);
    this.voteForm.controls['selectedOption'].setValue(option);
  }

  submitVote(): void {
    if (!this.voteForm.valid || !this.poll) return;

    const selectedOption = this.voteForm.value.selectedOption;
    this.pollService.vote(this.poll!.id, selectedOption).subscribe({
      next: () => {
        this.submitted = true;
        this.localStorageService.setItem(
          this.poll!.id.toString(),
          selectedOption
        );
        this.fetchPollResults();
      },
      error: this.handleError('Error submitting vote'),
    });
  }

  private fetchPollResults(): void {
    if (!this.poll) return;

    this.pollService.getPollOptionPercentageResults(this.poll!.id).subscribe({
      next: (percentages) => {
        this.votePercentages = percentages;
        this.listenForVoteUpdates();
      },
      error: this.handleError('Error fetching poll results'),
    });
  }

  private listenForVoteUpdates(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.pollService.listenForVoteUpdates().subscribe({
        next: (percentages) => {
          this.zone.run(() => {
            this.votePercentages = percentages;
          });
        },
        error: this.handleError('Error listening for vote results updates'),
      });
    }
  }

  private resetPoll(): void {
    this.submitted = false;
    this.voteForm.reset();
    this.votePercentages = {};
  }

  private handleError(message: string): (error: any) => void {
    return (error) => {
      console.error(`${message}:`, error);
    };
  }
}
