import { test, expect } from '@playwright/test'
import { injectAuth, ONE_PATIENT_PAGE } from './helpers'

test('register indiba session', async ({ page }) => {
  await injectAuth(page)

  await page.route('**/api/patients**', (route) =>
    route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: JSON.stringify(ONE_PATIENT_PAGE),
    }),
  )

  await page.route('**/api/indiba/create', (route) =>
    route.fulfill({ status: 201, body: '{}' }),
  )

  await page.goto('/patients')
  await page.goto('/indiba/register')

  await expect(page.getByRole('heading', { name: 'Register INDIBA Session' })).toBeVisible()
  await expect(page.getByRole('main').getByText('Anna Garcia')).toBeVisible()

  await page.fill('#sessionDate', '2024-01-15')
  await page.fill('#startTime', '09:00')
  await page.fill('#endTime', '10:00')
  await page.selectOption('#patientId', '1')
  await page.fill('#treatedArea', 'Lumbar spine')

  const [response] = await Promise.all([
    page.waitForResponse('**/api/indiba/create'),
    page.click('button[type="submit"]'),
  ])

  expect(response.status()).toBe(201)
  await expect(page).toHaveURL('/patients')
})
