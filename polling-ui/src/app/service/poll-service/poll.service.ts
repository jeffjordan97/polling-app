import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Poll, PollOptionPercentage } from '../../models/Poll';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class PollService {
  private apiUrl = environment.apiUrl;
  private pollUpdatesURL = environment.pollUpdatesURL;
  private pollResultsURL = environment.pollResultsURL;

  constructor(private http: HttpClient) {}

  getActivePoll(): Observable<Poll> {
    return this.http.get<Poll>(`${this.apiUrl}/polls/active`);
  }

  vote(pollId: number, option: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/votes`, { pollId, option });
  }

  getPollOptionPercentageResults(
    pollId: number
  ): Observable<PollOptionPercentage> {
    return this.http.get<PollOptionPercentage>(
      `${this.apiUrl}/votes/${pollId}/percentageResults`
    );
  }

  listenForPollUpdates(): Observable<Poll> {
    return new Observable((observer) => {
      const eventSource = new EventSource(this.pollUpdatesURL);
      eventSource.onmessage = (event) => {
        const data = JSON.parse(event.data);
        observer.next(data);
      };

      eventSource.onerror = (error) => {
        observer.error(error);
        eventSource.close();
      };

      return () => {
        eventSource.close();
      };
    });
  }

  listenForVoteUpdates(): Observable<PollOptionPercentage> {
    return new Observable((observer) => {
      const eventSource = new EventSource(this.pollResultsURL);
      eventSource.onmessage = (event) => {
        const data = JSON.parse(event.data);
        observer.next(data);
      };

      eventSource.onerror = (error) => {
        observer.error(error);
        eventSource.close();
      };

      return () => {
        eventSource.close();
      };
    });
  }
}
