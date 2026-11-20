import {useEffect,useState} from 'react'
import {citizenApi} from '../api/api.js'
import {ErrorBox} from './Badges.jsx'
const EMPTY={fullName:'',email:'',phone:'',aadhaarNumber:'',gender:'MALE',dateOfBirth:'',wardNumber:'',address:''}
export default function Citizens(){
 const [form,setForm]=useState(EMPTY),[list,setList]=useState([]),[search,setSearch]=useState(''),[error,setError]=useState(null),[message,setMessage]=useState('')
 const load=(q='')=>citizenApi.list(q).then(setList).catch(setError)
 useEffect(()=>{load()},[])
 const change=e=>setForm({...form,[e.target.name]:e.target.value})
 const submit=async e=>{e.preventDefault();setError(null);setMessage('');try{const c=await citizenApi.register({...form,wardNumber:Number(form.wardNumber)});setMessage(`Successfully registered ${c.fullName} · ${c.citizenCode}`);setForm(EMPTY);load(search)}catch(err){setError(err)}}
 return <><div className="page-heading"><div><h1>Citizen Registration</h1><p>Register citizens and maintain verified ward details</p></div></div>
 <form className="panel form-grid" onSubmit={submit}>
 <label>Full name<input name="fullName" value={form.fullName} onChange={change} required minLength="3"/></label>
 <label>Email<input name="email" type="email" value={form.email} onChange={change} required/></label>
 <label>Phone<input name="phone" value={form.phone} onChange={change} placeholder="10-digit mobile" required/></label>
 <label>Aadhaar number<input name="aadhaarNumber" value={form.aadhaarNumber} onChange={change} placeholder="12 digits (use dummy data)" required/></label>
 <label>Gender<select name="gender" value={form.gender} onChange={change}><option>MALE</option><option>FEMALE</option><option>OTHER</option></select></label>
 <label>Date of birth<input name="dateOfBirth" type="date" value={form.dateOfBirth} onChange={change} required/></label>
 <label>Ward number<input name="wardNumber" type="number" min="1" max="200" value={form.wardNumber} onChange={change} required/></label>
 <label>Address<input name="address" value={form.address} onChange={change} required/></label>
 <div className="form-actions"><button type="submit">Register Citizen</button></div>
 </form>{message&&<div className="success">{message}</div>}<ErrorBox error={error}/>
 <section className="panel"><div className="section-head"><h2>Registered Citizens ({list.length})</h2><div className="inline"><input placeholder="Search name" value={search} onChange={e=>setSearch(e.target.value)}/><button onClick={()=>load(search)}>Search</button></div></div>
 <div className="table-wrap"><table><thead><tr><th>Citizen Code</th><th>Name</th><th>Email</th><th>Phone</th><th>Aadhaar</th><th>Ward</th></tr></thead><tbody>
 {list.map(c=><tr key={c.id}><td>{c.citizenCode}</td><td>{c.fullName}</td><td>{c.email}</td><td>{c.phone}</td><td>{c.maskedAadhaar}</td><td>{c.wardNumber}</td></tr>)}
 {!list.length&&<tr><td colSpan="6" className="empty">No citizens found.</td></tr>}</tbody></table></div></section></>
}
