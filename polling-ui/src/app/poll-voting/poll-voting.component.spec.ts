import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PollVotingComponent } from './poll-voting.component';
import { ReactiveFormsModule } from '@angular/forms';
import { PollService } from '../service/poll-service/poll.service';
import { LocalStorageService } from '../service/local-storage-service/local-storage.service';
import { of, throwError } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { PLATFORM_ID } from '@angular/core';

describe('PollVotingComponent', () => {
  let component: PollVotingComponent;
  let fixture: ComponentFixture<PollVotingComponent>;
  let pollServiceMock: any;
  let localStorageServiceMock: any;
  const mockPoll = {
    id: 1,
    name: 'Sample Poll',
    options: ['Option 1', 'Option 2'],
  };
  const mockPercentages = {
    'Option 1': 60,
    'Option 2': 40,
  };

  beforeEach(async () => {
    pollServiceMock = jasmine.createSpyObj('PollService', [
      'getActivePoll',
      'vote',
      'getPollOptionPercentageResults',
      'listenForPollUpdates',
      'listenForVoteUpdates',
    ]);
    localStorageServiceMock = jasmine.createSpyObj('LocalStorageService', [
      'getItem',
      'setItem',
    ]);

    await TestBed.configureTestingModule({
      imports: [
        PollVotingComponent,
        HttpClientTestingModule,
        ReactiveFormsModule,
      ],
      providers: [
        { provide: PollService, useValue: pollServiceMock },
        { provide: LocalStorageService, useValue: localStorageServiceMock },
        { provide: PLATFORM_ID, useValue: 'browser' },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(PollVotingComponent);
    component = fixture.componentInstance;

    pollServiceMock.getActivePoll.and.returnValue(of(mockPoll));
    pollServiceMock.listenForPollUpdates.and.returnValue(of(mockPoll));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load active poll on init', () => {
    component.ngOnInit();

    expect(pollServiceMock.getActivePoll).toHaveBeenCalled();
    expect(pollServiceMock.listenForPollUpdates).toHaveBeenCalled();
    expect(component.poll).toEqual(mockPoll);
  });

  it('should handle errors when loading active poll', () => {
    pollServiceMock.getActivePoll.and.returnValue(
      throwError(() => new Error('Error'))
    );
    pollServiceMock.listenForPollUpdates.and.returnValue(of(null));
    pollServiceMock.listenForVoteUpdates.and.returnValue(of(null));

    component.ngOnInit();
    fixture.detectChanges();

    expect(pollServiceMock.getActivePoll).toHaveBeenCalled();
    expect(component.poll).toBeNull();
  });

  it('should submit a vote and fetch updated percentages', () => {
    component.poll = mockPoll;
    component.voteForm.controls['selectedOption'].setValue('Option 1');
    pollServiceMock.getActivePoll.and.returnValue(of(mockPoll));
    pollServiceMock.vote.and.returnValue(of(null));
    pollServiceMock.getPollOptionPercentageResults.and.returnValue(
      of(mockPercentages)
    );
    pollServiceMock.listenForVoteUpdates.and.returnValue(of(mockPercentages));

    fixture.detectChanges();

    component.submitVote();

    expect(pollServiceMock.vote).toHaveBeenCalledWith(1, 'Option 1');
    expect(localStorageServiceMock.setItem).toHaveBeenCalledWith(
      '1',
      'Option 1'
    );
    expect(pollServiceMock.getPollOptionPercentageResults).toHaveBeenCalledWith(
      mockPoll.id
    );
    expect(component.votePercentages).toEqual(mockPercentages);
    expect(component.submitted).toBeTrue();
  });

  it('should handle errors when submitting a vote', () => {
    component.poll = mockPoll;
    component.voteForm.controls['selectedOption'].setValue('Option 1');
    pollServiceMock.getActivePoll.and.returnValue(of(mockPoll));
    pollServiceMock.listenForPollUpdates.and.returnValue(of(null));
    pollServiceMock.vote.and.returnValue(throwError(() => new Error('Error')));
    fixture.detectChanges();

    component.submitVote();

    expect(pollServiceMock.vote).toHaveBeenCalledWith(1, 'Option 1');
    expect(component.submitted).toBeFalse();
  });

  it('should fetch poll results and update percentages', () => {
    const mockResults = { 'Option 1': 50, 'Option 2': 50 };
    component.poll = mockPoll;
    pollServiceMock.getActivePoll.and.returnValue(of(mockPoll));
    pollServiceMock.getPollOptionPercentageResults.and.returnValue(
      of(mockResults)
    );
    pollServiceMock.listenForVoteUpdates.and.returnValue(of(mockResults));
    fixture.detectChanges();

    component['fetchPollResults']();

    expect(pollServiceMock.getPollOptionPercentageResults).toHaveBeenCalledWith(
      1
    );
    expect(component.votePercentages).toEqual(mockResults);
  });
});
