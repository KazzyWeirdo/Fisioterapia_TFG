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

  let indibaPostCalled = false
  await page.route('**/api/indiba/create', (route) => {
    indibaPostCalled = true
    route.fulfill({ status: 201, body: '{}' })
  })

  await page.goto('/indiba/register')

  await expect(page.getByRole('heading', { name: 'Register INDIBA Session' })).toBeVisible()
  await expect(page.getByRole('main').getByText('Anna Garcia')).toBeVisible()

  await page.fill('#beginSession', '2024-01-15T09:00')
  await page.fill('#endSession', '2024-01-15T10:00')
  await page.selectOption('#patientId', '1')
  await page.fill('#treatedArea', 'Lumbar spine')

  await page.click('button[type="submit"]')

  expect(indibaPostCalled).toBe(true)
  await expect(page).not.toHaveURL('/indiba/register')
})
