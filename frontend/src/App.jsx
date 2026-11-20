import { useState } from 'react'
import Dashboard from './components/Dashboard.jsx'
import Citizens from './components/Citizens.jsx'
import Grievances from './components/Grievances.jsx'
const MENU = [{key:'dashboard',label:'Dashboard'},{key:'citizens',label:'Citizens'},{key:'grievances',label:'Grievances'}]
export default function App() {
 const [page,setPage]=useState('dashboard')
 return <div className="app">
  <header className="topbar"><div className="brand">CivicPulse Nexus</div><div className="subtitle">Milestone 1 · Citizen & Grievance Management</div><div className="admin">Municipal Admin</div></header>
  <div className="layout"><nav className="sidebar">{MENU.map(m=><button key={m.key} className={page===m.key?'active':''} onClick={()=>setPage(m.key)}>{m.label}</button>)}</nav>
   <main className="content">{page==='dashboard'&&<Dashboard onNavigate={setPage}/>}{page==='citizens'&&<Citizens/>}{page==='grievances'&&<Grievances/>}</main>
  </div>
  <footer> CivicPulse Nexus · Smart Governance Platform · Milestone 1</footer>
 </div>
}
