import {useEffect,useState} from 'react';
import {Keyboard,ArrowRight,Volume2,VolumeX,LogOut,History,Trophy,Timer} from 'lucide-react';
import {api,refreshCsrf} from './api';
import {isMuted,setMuted,unlockSound} from './sound';
import Lobby from './Lobby';

export default function App(){
 const [user,setUser]=useState(null),[loading,setLoading]=useState(true);
 const [mode,setMode]=useState('login'),[page,setPage]=useState('play');
 const [email,setEmail]=useState(''),[password,setPassword]=useState(''),[name,setName]=useState('');
 const [join,setJoin]=useState(false),[code,setCode]=useState(''),[duration,setDuration]=useState(60);
 const [busy,setBusy]=useState(false),[error,setError]=useState(''),[history,setHistory]=useState(null);
 const [muted,setMute]=useState(isMuted),[session,setSession]=useState(null);
 useEffect(()=>{
  let live=true;
  async function init(){
   try {
    await refreshCsrf();const u=await api('/api/auth/me');
    if(!live)return;
    setUser(u);
    try {
     const saved=JSON.parse(sessionStorage.getItem('typebaazi-v2-room'));
     if(saved?.userId===u.id)setSession(saved);
    }catch{/* Ignore old or damaged browser storage. */}
   }catch(e){if(live && e.status!==401)setError(e.message);}
   finally{if(live)setLoading(false);}
  }
  init();return()=>{live=false;};
 },[]);
 function remember(s){
  const value=s?{...s,userId:user.id}:null;
  if(value)sessionStorage.setItem('typebaazi-v2-room',JSON.stringify(value));
  else sessionStorage.removeItem('typebaazi-v2-room');
  setSession(value);
 }
 async function auth(event){
  event.preventDefault();if(busy)return;setBusy(true);setError('');unlockSound();
  try{
   const u=await api(`/api/auth/${mode}`,{method:'POST',body:{email,password,username:name}});
   await refreshCsrf();setUser(u);setPassword('');setSession(null);setPage('play');
   sessionStorage.removeItem('typebaazi-v2-room');
  }catch(e){setError(e.message);}finally{setBusy(false);}
 }
 async function enter(event){
  event.preventDefault();if(busy)return;setBusy(true);setError('');unlockSound();
  try{
   const s=await api(join?`/api/rooms/${encodeURIComponent(code.trim().toUpperCase())}/join`:'/api/rooms',
    {method:'POST',body:join?undefined:{durationSeconds:duration}});
   remember(s);
  }catch(e){setError(e.message);}finally{setBusy(false);}
 }
 async function showHistory(){
  setPage('history');setHistory(null);setError('');
  try{setHistory(await api('/api/history'));}catch(e){setError(e.message);}
 }
 async function logout(){
  setBusy(true);setError('');
  try{
   await api('/api/auth/logout',{method:'POST'});remember(null);setUser(null);setHistory(null);
   await refreshCsrf();
  }catch(e){setError(e.message);}finally{setBusy(false);}
 }
 if(loading)return <main className="loading">Opening TypeBaazi…</main>;
 return <div className="page-background app-shell">
  <header className="topbar">
   <div className="brand"><span className="brand-icon"><Keyboard size={23}/></span>Type<span>Baazi</span></div>
   <nav aria-label="Main navigation">
    {user&&!session&&<><button className={page==='play'?'nav active':'nav'} onClick={()=>{setPage('play');setError('');}}>Play</button>
     <button className={page==='history'?'nav active':'nav'} onClick={showHistory}><History size={16}/> Profile & history</button></>}
    <button className="icon-button" aria-label={muted?'Enable sound':'Mute sound'} title={muted?'Enable sound':'Mute sound'}
     onClick={()=>{unlockSound();setMuted(!muted);setMute(!muted);}}>{muted?<VolumeX size={19}/>:<Volume2 size={19}/>}</button>
    {user&&!session&&<button className="icon-button" disabled={busy} onClick={logout} title="Log out" aria-label="Log out"><LogOut size={18}/></button>}
   </nav>
  </header>
  {session&&user?<Lobby session={session} user={user} onLeave={()=>remember(null)}/>:
   !user?<main className="home-grid enter-page">
    <section className="hero"><p className="eyebrow">A FRIENDLY RIVALRY STARTS HERE</p>
     <h1>Dosti apni<br/>jagah.<br/><em>Speed apni<br/>jagah.</em></h1>
     <p className="intro">Same words. Same clock. A little competition and a lot of bragging rights.</p>
     <div className="keys" aria-hidden="true">{'TYPE'.split('').map(c=><span className="keycap" key={c}>{c}</span>)}</div>
    </section>
    <section className="panel auth-panel"><p className="eyebrow">YOUR PLAYER PROFILE</p><h2>{mode==='login'?'Welcome back.':'Join the starting line.'}</h2>
     <p className="muted">Save your wins. Find your rhythm.</p>
     <div className="segmented">{['login','register'].map(m=><button key={m} disabled={busy} aria-pressed={mode===m} className={mode===m?'selected':''}
      onClick={()=>{setMode(m);setError('');}}>{m==='login'?'Log in':'Create account'}</button>)}</div>
     <form onSubmit={auth}>
      {mode==='register'&&<label>Username<input required minLength={2} maxLength={20} value={name} onChange={e=>setName(e.target.value)} autoComplete="nickname" placeholder="Your racing name"/></label>}
      <label>Email<input type="email" required maxLength={254} autoComplete="email" value={email} onChange={e=>setEmail(e.target.value)} placeholder="you@example.com"/></label>
      <label>Password<input type="password" required minLength={mode==='register'?8:undefined} maxLength={72} autoComplete={mode==='login'?'current-password':'new-password'} value={password} onChange={e=>setPassword(e.target.value)} placeholder={mode==='login'?'Your password':'At least 8 characters'}/></label>
      <button className="primary" disabled={busy}>{busy?'Please wait…':mode==='login'?'Let’s play':'Create my profile'}<ArrowRight size={17}/></button>
     </form>
     {error&&<p className="error" role="alert">{error}</p>}
    </section>
   </main>:page==='history'?<main className="content enter-page"><p className="eyebrow">YOUR TRACK RECORD</p><h1 className="page-title">{user.username}</h1><p className="muted">{user.email}</p>
    {error&&<p role="alert" className="error">{error}</p>}
    {!history&&!error?<p className="muted">Loading history…</p>:history&&<>
     <div className="stats">{[['Races played',history.played],['Wins',history.wins],['Best WPM',history.bestWpm]].map(([label,value])=><div className="panel stat" key={label}><span>{label}</span><strong>{value}</strong></div>)}</div>
     <div className="panel history-panel"><h2>Past rivalries</h2><p className="muted small">Dates and times use your device’s timezone.</p>
      {history.races.length===0?<div className="empty"><Trophy size={30}/><p>Your first race belongs here.</p><button className="secondary" onClick={()=>setPage('play')}>Start a race</button></div>:
       <div className="table-wrap"><table><thead><tr><th>Opponent</th><th>Result / winner</th><th>WPM · you / them</th><th>Accuracy</th><th>Duration</th><th>Date & time</th></tr></thead>
        <tbody>{history.races.map(r=><tr key={r.id}><td>{r.opponent}</td><td><span className={r.outcome==='Won'?'positive':r.outcome==='Lost'?'negative':'accent'}>{r.outcome}</span><small>{r.winner}</small></td><td>{r.wpm} / {r.opponentWpm}</td><td>{r.accuracy}%<small>{r.mistakes} mistakes</small></td><td>{r.durationSeconds}s</td><td>{new Date(r.finishedAt).toLocaleString()}</td></tr>)}</tbody></table></div>}
     </div></>}
   </main>:<main className="home-grid enter-page"><section className="hero"><p className="eyebrow">WELCOME, {user.username.toUpperCase()}</p><h1>Friendly faces.<br/><em>Fast fingers.</em></h1><p className="intro">Set the clock. Share the room. Let your typing settle the debate.</p><div className="keys" aria-hidden="true">{'RACE'.split('').map(c=><span key={c} className="keycap">{c}</span>)}</div></section>
    <section className="panel auth-panel"><p className="eyebrow">THE STARTING LINE</p><h2>Ready to face off?</h2><p className="muted">Playing as <span className="accent">{user.username}</span></p>
     <div className="segmented"><button disabled={busy} aria-pressed={!join} className={!join?'selected':''} onClick={()=>{setJoin(false);setError('');}}>Create room</button><button disabled={busy} aria-pressed={join} className={join?'selected':''} onClick={()=>{setJoin(true);setError('');}}>Join room</button></div>
     <form onSubmit={enter}>{join?<label>Room code<input required maxLength={6} value={code} onChange={e=>setCode(e.target.value.toUpperCase())} autoComplete="off" placeholder="ABC123"/></label>:<label>Race duration<select value={duration} onChange={e=>setDuration(Number(e.target.value))}>{[30,60,90,120].map(s=><option key={s} value={s}>{s} seconds</option>)}</select></label>}
      <button className="primary" disabled={busy}>{busy?'Connecting…':join?'Join the race':'Create a room'}<ArrowRight size={17}/></button></form>
     {error&&<p className="error" role="alert">{error}</p>}<p className="small muted hint"><Timer size={14}/> Your friend readies up. You start the race.</p>
    </section></main>}
  <footer>TypeBaazi · Every keystroke counts.<span>Built for friendly rivalries.</span></footer>
 </div>;
}
