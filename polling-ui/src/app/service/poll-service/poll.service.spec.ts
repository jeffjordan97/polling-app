import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { PollService } from '../poll-service/poll.service';
import { environment } from '../../../environments/environment';
import { Poll } from '../../models/Poll';

describe('PollService', () => {
  let service: PollService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PollService],
    });
    service = TestBed.inject(PollService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get the active poll', () => {
    const mockPoll = { id: 1, name: 'Test Poll', options: ['One', 'Two'] };

    service.getActivePoll().subscribe((poll) => {
      expect(poll).toEqual(mockPoll);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/polls/active`);
    expect(req.request.method).toBe('GET');
    req.flush(mockPoll);
  });

  it('should submit a vote', () => {
    const pollId = 1;
    const option = 'One';

    service.vote(pollId, option).subscribe((response) => {
      expect(response).toBeNull();
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/votes`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ pollId, option });
    req.flush(null);
  });

  it('should fetch vote percentage results', () => {
    const pollId = 1;
    const mockPercentages = { One: 50, Two: 50 };

    service.getPollOptionPercentageResults(pollId).subscribe((percentages) => {
      expect(percentages).toEqual(mockPercentages);
    });

    const req = httpMock.expectOne(
      `${environment.apiUrl}/votes/${pollId}/percentageResults`
    );
    expect(req.request.method).toBe('GET');
    req.flush(mockPercentages);
  });

  it('should handle errors for getActivePoll', () => {
    service.getActivePoll().subscribe(
      () => fail('Expected an error, but got a poll'),
      (error) => expect(error.status).toBe(404)
    );

    const req = httpMock.expectOne(`${environment.apiUrl}/polls/active`);
    req.flush('Poll not found', { status: 404, statusText: 'Not Found' });
  });
});
