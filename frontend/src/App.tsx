import { BrowserRouter, Routes, Route, Navigate, Outlet } from 'react-router-dom'
import { AuthProvider, useAuth } from './contexts/AuthContext'
import LoginPage from './pages/LoginPage'
import ForgotPasswordPage from './pages/ForgotPasswordPage'

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

          {/* Protected */}
          <Route element={<PrivateRoute />}>
            <Route index element={<Navigate to="/patients" replace />} />
            <Route path="/patients" element={<div>Patients</div>} />
            <Route path="/patients/:id" element={<div>Patient Detail</div>} />
            <Route path="/patients/:id/indiba" element={<div>INDIBA Sessions</div>} />
            <Route path="/patients/:id/pni" element={<div>PNI Reports</div>} />
            <Route path="/patients/:id/training" element={<div>Training Sessions</div>} />
            <Route path="/statistics" element={<div>Statistics</div>} />
            <Route path="/audit" element={<div>Audit Log</div>} />
          </Route>

          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}
