import { test, expect } from '@playwright/test'
import { FIXTURE_JWT, EMPTY_PATIENTS_PAGE } from './helpers'

test('login happy path', async ({ page }) => {
  await page.route('**/api/physiotherapist/login', (route) =>
    route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: JSON.stringify({ access_token: FIXTURE_JWT }),
    }),
  )

  await page.route('**/api/patients**', (route) =>
    route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: JSON.stringify(EMPTY_PATIENTS_PAGE),
    }),
  )

  await page.goto('/login')

  await page.fill('#email', 'anna@clinic.com')
  await page.fill('#password', 'password123')
  await page.click('button[type="submit"]')

  await expect(page).toHaveURL('/patients')
  await expect(page.getByRole('heading', { name: 'Patient Records' })).toBeVisible()
})
