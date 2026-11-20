async function request(url, options = {}) {
  const res = await fetch(url, { headers: { 'Content-Type': 'application/json' }, ...options })
  const text = await res.text()
  let data = null
  try { data = text ? JSON.parse(text) : null } catch { data = { message: text } }
  if (!res.ok) {
    const msg = data?.message || data?.detail || `HTTP ${res.status}`
    const e = new Error(msg); e.fieldErrors = data?.fieldErrors || {}; e.status = res.status; throw e
  }
  return data
}
const post = (url, body) => request(url, { method: 'POST', body: JSON.stringify(body ?? {}) })
const patch = (url, body) => request(url, { method: 'PATCH', body: JSON.stringify(body) })
export const citizenApi = {
  list: (name) => request(`/api/citizens${name ? `?name=${encodeURIComponent(name)}` : ''}`),
  register: (data) => post('/api/citizens', data)
}
export const grievanceApi = {
  list: (status) => request(`/api/grievances${status ? `?status=${status}` : ''}`),
  get: (id) => request(`/api/grievances/${id}`),
  meta: () => request('/api/grievances/meta'),
  history: (id) => request(`/api/grievances/${id}/history`),
  register: (data) => post('/api/grievances', data),
  assign: (id, data) => post(`/api/grievances/${id}/assign`, data),
  status: (id, data) => patch(`/api/grievances/${id}/status`, data),
  escalate: (id, reason) => post(`/api/grievances/${id}/escalate`, { reason }),
  simulate: (id) => post(`/api/grievances/${id}/simulate-breach`)
}
export const dashboardApi = { summary: () => request('/api/dashboard/summary') }
