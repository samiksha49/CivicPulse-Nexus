export function ErrorBox({ error }) {
  if (!error) return null
  return <div className="error"><b>{error.message || String(error)}</b>{error.fieldErrors && Object.keys(error.fieldErrors).length > 0 && <ul>{Object.entries(error.fieldErrors).map(([k,v]) => <li key={k}>{k}: {v}</li>)}</ul>}</div>
}
export function StatusBadge({ status }) { return <span className={`badge st-${status}`}>{status}</span> }
export function SlaBadge({ sla }) { return <span className={`badge sla-${sla}`}>{sla?.replace('_',' ')}</span> }
export function StatCard({label,value,hint,tone=''}) { return <div className={`stat ${tone}`}><div className="stat-label">{label}</div><div className="stat-value">{value ?? '—'}</div><div className="hint">{hint}</div></div> }
