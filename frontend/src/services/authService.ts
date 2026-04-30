import apiClient from '../api/client'

export async function login(email: string, password: string): Promise<string> {
  localStorage.removeItem('access_token')
  const response = await apiClient.post<{ access_token: string }>(
    '/physiotherapist/login',
    { email, password },
  )
  return response.data.access_token
}

export async function forgotPassword(email: string): Promise<void> {
  await apiClient.post('/password/forgot', { email })
}

export async function resetPassword(token: string, newPassword: string): Promise<void> {
  await apiClient.post('/password/reset', { token, newPassword })
}
