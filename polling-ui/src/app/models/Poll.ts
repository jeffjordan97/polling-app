export interface Poll {
  id: number;
  name: string;
  options: string[];
}

export type PollOptionPercentage = { [option: string]: number };
