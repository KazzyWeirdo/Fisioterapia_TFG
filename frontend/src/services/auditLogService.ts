import apiClient from '../api/client'

export interface AuditLogSummary {
  id: number
  entityName: string
  action: string
  timestamp: string
  details: string
  user: string
}

export interface AuditLogsPage {
  content: AuditLogSummary[]
  totalElements: number
  totalPages: number
  pageNumber: number
  isLast: boolean
}

const EMPTY_PAGE: AuditLogsPage = {
  content: [],
  totalElements: 0,
  totalPages: 0,
  pageNumber: 0,
  isLast: true,
}

export async function getAuditLogs(
  page: number,
  size = 10,
): Promise<AuditLogsPage> {
  const response = await apiClient.get<AuditLogsPage>('/auditlogs/list', {
    params: { page, size },
  })
  if (response.status === 204 || !response.data) return EMPTY_PAGE
  return response.data
}
