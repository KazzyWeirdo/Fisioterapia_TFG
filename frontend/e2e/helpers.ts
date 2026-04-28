import type { Page } from '@playwright/test'

// Fixture JWT with payload { sub: "1", name: "Anna", surname: "Garcia" }
// Signature is ignored client-side — "fake-sig" is fine.
export const FIXTURE_JWT =
  'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9' +
  '.eyJzdWIiOiIxIiwibmFtZSI6IkFubmEiLCJzdXJuYW1lIjoiR2FyY2lhIn0' +
  '.fake-sig'

export const FIXTURE_PATIENT = {
  id: 1,
  name: 'Joan',
  surname: 'Puig',
  secondSurname: null,
}

export const EMPTY_PATIENTS_PAGE = {
  content: [],
  totalElements: 0,
  totalPages: 0,
  number: 0,
}

export const ONE_PATIENT_PAGE = {
  content: [FIXTURE_PATIENT],
  totalElements: 1,
  totalPages: 1,
  number: 0,
}

/** Inject the fixture JWT into localStorage before the page loads. */
export async function injectAuth(page: Page): Promise<void> {
  await page.addInitScript((jwt: string) => {
    localStorage.setItem('access_token', jwt)
  }, FIXTURE_JWT)
}
