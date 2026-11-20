import {useEffect,useState} from 'react'
import {citizenApi,grievanceApi} from '../api/api.js'
import {ErrorBox,StatusBadge,SlaBadge} from './Badges.jsx'
const EMPTY={citizenId:'',category:'WATER_SUPPLY',severity:'HIGH',title:'',description:'',location:''}
export default function Grievances(){
 const [meta,setMeta]=useState(null),[citizens,setCitizens]=useState([]),[list,setList]=useState([]),[filter,setFilter]=useState(''),[form,setForm]=useState(EMPTY),[selected,setSelected]=useState(null),[detail,setDetail]=useState(null),[history,setHistory]=useState([]),[error,setError]=useState(null),[officer,setOfficer]=useState(''),[department,setDepartment]=useState(''),[notes,setNotes]=useState('')
 const load=()=>grievanceApi.list(filter).then(setList).catch(setError)
 const loadDetail=async id=>{setSelected(id);try{const [g,h]=await Promise.all([grievanceApi.get(id),grievanceApi.history(id)]);setDetail(g);setHistory(h);setOfficer(g.assignedOfficer||'');setDepartment(g.departmentName||'')}catch(e){setError(e)}}
 useEffect(()=>{grievanceApi.meta().then(setMeta).catch(setError);citizenApi.list().then(setCitizens).catch(()=>{})},[])
 useEffect(()=>{load()},[filter])
 const change=e=>setForm({...form,[e.target.name]:e.target.value})
 const submit=async e=>{e.preventDefault();setError(null);try{const g=await grievanceApi.register({...form,citizenId:Number(form.citizenId)});setForm(EMPTY);await load();loadDetail(g.id)}catch(e){setError(e)}}
 const act=async fn=>{setError(null);try{await fn();await load();if(selected)await loadDetail(selected)}catch(e){setError(e)}}
 return <><div className="page-heading"><div><h1>Grievance Redressal</h1><p>Register, assign, track and resolve citizen complaints</p></div></div>
 <form className="panel form-grid" onSubmit={submit}>
 <label>Citizen<select name="citizenId" value={form.citizenId} onChange={change} required><option value="">Select citizen</option>{citizens.map(c=><option key={c.id} value={c.id}>{c.fullName} ({c.citizenCode})</option>)}</select></label>
 <label>Category<select name="category" value={form.category} onChange={change}>{(meta?.categories||['WATER_SUPPLY','ROADS','SANITATION','ELECTRICITY','DRAINAGE','STREET_LIGHTS','OTHER']).map(x=><option key={x}>{x}</option>)}</select></label>
 <label>Severity<select name="severity" value={form.severity} onChange={change}>{(meta?.severities||['LOW','MEDIUM','HIGH','CRITICAL']).map(x=><option key={x}>{x}</option>)}</select></label>
 <label>Location<input name="location" value={form.location} onChange={change}/></label>
 <label className="wide">Title<input name="title" value={form.title} onChange={change} required/></label>
 <label className="wide">Description<textarea name="description" value={form.description} onChange={change} minLength="10" required rows="3"/></label>
 <div className="form-actions"><button type="submit">Register Grievance</button></div>
 </form><ErrorBox error={error}/>
 <div className="work-grid"><section className="panel"><div className="section-head"><h2>Grievances ({list.length})</h2><select value={filter} onChange={e=>setFilter(e.target.value)}><option value="">All statuses</option>{['REGISTERED','ASSIGNED','IN_PROGRESS','RESOLVED','CLOSED','REJECTED'].map(x=><option key={x}>{x}</option>)}</select></div>
 <div className="table-wrap"><table><thead><tr><th>Number / Title</th><th>Citizen</th><th>Status</th><th>SLA</th></tr></thead><tbody>{list.map(g=><tr key={g.id} className={selected===g.id?'selected':''} onClick={()=>loadDetail(g.id)}><td><b>{g.grievanceNumber}</b><small>{g.title}</small></td><td>{g.citizenName}</td><td><StatusBadge status={g.status}/></td><td><SlaBadge sla={g.slaStatus}/></td></tr>)}{!list.length&&<tr><td colSpan="4" className="empty">No grievances yet.</td></tr>}</tbody></table></div></section>
 <section className="panel detail-panel"><h2>Workflow & Audit</h2>{!detail?<p className="muted">Select a grievance to view details and actions.</p>:<>
 <h3>{detail.grievanceNumber} <StatusBadge status={detail.status}/></h3><h4>{detail.title}</h4><p>{detail.description}</p>
 <p><b>Citizen:</b> {detail.citizenName} · Ward {detail.wardNumber}</p><p><b>Department:</b> {detail.departmentName||'—'} · <b>Officer:</b> {detail.assignedOfficer||'—'}</p>
 <p><b>SLA:</b> {detail.slaHours} hours · {new Date(detail.slaDueAt).toLocaleString()} <SlaBadge sla={detail.slaStatus}/></p><p><b>Escalation level:</b> {detail.escalationLevel}</p>
 {(detail.status==='REGISTERED'||detail.status==='ASSIGNED')&&<div className="action-box"><h4>Assign Officer</h4><input placeholder="Officer name" value={officer} onChange={e=>setOfficer(e.target.value)}/><select value={department} onChange={e=>setDepartment(e.target.value)}><option value="">Keep department</option>{(meta?.departments||[]).map(d=><option key={d}>{d}</option>)}</select><button onClick={()=>act(()=>grievanceApi.assign(selected,{officer,department}))}>Assign / Reassign</button></div>}
 <div className="action-box"><h4>Workflow action</h4><textarea placeholder="Resolution notes / remarks (minimum 10 characters to resolve)" value={notes} onChange={e=>setNotes(e.target.value)} rows="2"/>
 {detail.status==='ASSIGNED'&&<button onClick={()=>act(()=>grievanceApi.status(selected,{status:'IN_PROGRESS',remarks:'Work started'}))}>Start Work</button>}
 {detail.status==='REGISTERED'&&<button onClick={()=>act(()=>grievanceApi.status(selected,{status:'IN_PROGRESS',remarks:'Work started'}))}>Start Work</button>}
 {detail.status==='IN_PROGRESS'&&<button onClick={()=>act(()=>grievanceApi.status(selected,{status:'RESOLVED',resolutionNotes:notes,remarks:'Resolved by officer'}))}>Resolve</button>}
 {detail.status==='RESOLVED'&&<button onClick={()=>act(()=>grievanceApi.status(selected,{status:'CLOSED',remarks:'Closed by admin'}))}>Close</button>}
 {!['CLOSED','REJECTED'].includes(detail.status)&&<button className="warn" onClick={()=>act(()=>grievanceApi.escalate(selected,'Manual escalation by admin'))}>Escalate</button>}
 {!['RESOLVED','CLOSED','REJECTED'].includes(detail.status)&&<button className="danger" onClick={()=>act(()=>grievanceApi.simulate(selected))}>Simulate SLA Breach</button>}
 </div><h4>Audit Timeline</h4><ul className="timeline">{history.map(h=><li key={h.id}><small>{h.at?new Date(h.at).toLocaleString():''}</small><b>{h.action}</b><span>{h.fromStatus&&`${h.fromStatus} → `}{h.toStatus||''}</span><p>{h.remarks} · {h.performedBy}</p></li>)}</ul>
 </>}</section></div></>
}
