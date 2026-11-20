import {useEffect,useState} from 'react'
import {dashboardApi,grievanceApi} from '../api/api.js'
import {ErrorBox,StatCard,StatusBadge,SlaBadge} from './Badges.jsx'
export default function Dashboard({onNavigate}) {
 const [summary,setSummary]=useState(null),[latest,setLatest]=useState([]),[error,setError]=useState(null)
 const load=()=>Promise.all([dashboardApi.summary(),grievanceApi.list()]).then(([s,l])=>{setSummary(s);setLatest(l.slice(0,6))}).catch(setError)
 useEffect(()=>{load()},[])
 return <><div className="page-heading"><div><h1>Registration & Redressal</h1><p>Live overview of citizen services and grievance performance</p></div><button onClick={()=>onNavigate('grievances')}>Manage Grievances</button></div>
 <ErrorBox error={error}/>{summary&&<div className="stats">
 <StatCard label="Registered Citizens" value={summary.registeredCitizens<0?'Service offline':summary.registeredCitizens} hint="Total citizens"/>
 <StatCard label="Grievances / Month" value={summary.grievancesThisMonth} hint="Current month"/>
 <StatCard label="Resolution Rate" value={`${summary.resolutionRate}%`} hint="Resolved / closed" tone="good"/>
 <StatCard label="Open Grievances" value={summary.openGrievances} hint="Needs action"/>
 <StatCard label="SLA Breached" value={summary.slaBreachedOpen} hint="Open and overdue" tone="bad"/>
 <StatCard label="SLA Compliance" value={`${summary.slaCompliance}%`} hint="Current measure" tone="good"/>
 </div>}
 <section className="panel"><div className="section-head"><h2>Recent Grievances</h2><button className="secondary" onClick={load}>Refresh</button></div>
 <div className="table-wrap"><table><thead><tr><th>Grievance ID</th><th>Citizen</th><th>Category</th><th>Department</th><th>Status</th><th>SLA</th></tr></thead><tbody>
 {latest.map(g=><tr key={g.id}><td>{g.grievanceNumber}</td><td>{g.citizenName}<small>{g.citizenCode}</small></td><td>{g.category}</td><td>{g.departmentName}</td><td><StatusBadge status={g.status}/></td><td><SlaBadge sla={g.slaStatus}/></td></tr>)}
 {!latest.length&&<tr><td colSpan="6" className="empty">No grievances registered yet.</td></tr>}
 </tbody></table></div></section></>
}
