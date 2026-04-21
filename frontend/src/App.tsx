import { BrowserRouter, Routes, Route, Navigate, Outlet } from 'react-router-dom'
import { AuthProvider, useAuth } from './contexts/AuthContext'
import LoginPage from './pages/LoginPage'
import ForgotPasswordPage from './pages/ForgotPasswordPage'
import ResetPasswordPage from './pages/ResetPasswordPage'
import DashboardLayout from './components/layout/DashboardLayout'
import PatientsPage from './pages/PatientsPage'
import PatientDetailPage from './pages/PatientDetailPage'
import RegisterPatientPage from './pages/RegisterPatientPage'
import AuditLogPage from './pages/AuditLogPage'

function PrivateRoute() {
  const { token } = useAuth()
  return token ? <Outlet /> : <Navigate to="/login" replace />
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* Public */}
          <Route path="/login" element={<LoginPage />} />
          <Route path="/forgot-password" element={<ForgotPasswordPage />} />
          <Route path="/reset-password" element={<ResetPasswordPage />} />

          {/* Protected */}
          <Route element={<PrivateRoute />}>
            <Route element={<DashboardLayout />}>
              <Route index element={<Navigate to="/patients" replace />} />
              <Route path="/patients" element={<PatientsPage />} />
              <Route path="/patients/register" element={<RegisterPatientPage />} />
              <Route path="/patients/:id" element={<PatientDetailPage />} />
              <Route path="/patients/:id/indiba" element={<div>INDIBA Sessions</div>} />
              <Route path="/patients/:id/pni" element={<div>PNI Reports</div>} />
              <Route path="/patients/:id/training" element={<div>Training Sessions</div>} />
              <Route path="/statistics" element={<div>Statistics</div>} />
              <Route path="/audit" element={<AuditLogPage />} />
            </Route>
          </Route>

          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}
