declare function dateWizard(date: Date, format: string): string;

declare namespace dateWizard {
  const pad: (ident: number) => string;

  interface DateDetails {
    year: number;
    month: number;
    date: number;
    hours: number;
    minutes: number;
    seconds: number;
  }

  function dateDetails(date: Date): DateDetails;
  function utcDateDetails(date: Date): DateDetails;
}

export = dateWizard;
