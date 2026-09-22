import {useEffect,useRef,useState} from 'react';
import {Copy,UserRound,Check,Timer,Crown} from 'lucide-react';
import {api} from './api';
import RaceResults from './RaceResults';
import {unlockSound,playSound} from './sound';

export default function Lobby({session,user,onLeave}){
 const [room,setRoom]=useState(null),[error,setError]=useState(''),[syncError,setSyncError]=useState('');
 const [typed,setTyped]=useState(''),[now,setNow]=useState(Date.now());
 const [busy,setBusy]=useState(false),[copied,setCopied]=useState(false);
 const offset=useRef(0),input=useRef(null),cursor=useRef(null),roomRef=useRef(null);
 const initialized=useRef(false),sequence=useRef(-1),draft=useRef(''),dirty=useRef(false);
 const alive=useRef(true),lastSound=useRef('');
 const base=`/api/rooms/${session.code}`;
 function accept(data){
  offset.current=data.serverTime-Date.now();roomRef.current=data;setRoom(data);
  if(!initialized.current){
   initialized.current=true;sequence.current=data.yourSequence;
   draft.current=data.yourText;setTyped(data.yourText);
  }
 }
 useEffect(()=>{
  alive.current=true;let stopped=false,timer;
  const controller=new AbortController();
  async function poll(){
   try{
    const data=await api(`/api/rooms/${session.code}`,{signal:controller.signal});
    if(!stopped){accept(data);setError('');}
   }catch(e){if(!stopped)setError(e.message);}
   finally{if(!stopped)timer=setTimeout(poll,500);}
  }
  poll();
  return()=>{stopped=true;alive.current=false;clearTimeout(timer);controller.abort();};
 },[session.code]);
 useEffect(()=>{
  const timer=setInterval(()=>setNow(Date.now()+offset.current),100);
  return()=>clearInterval(timer);
 },[]);
 // Coalesce typing updates and send them in order; keep failed updates for retry.
 useEffect(()=>{
  let stopped=false,sending=false;
  const timer=setInterval(async()=>{
   const r=roomRef.current;
   if(stopped||sending||!dirty.current||!r||r.status!=='RACING')return;
   const text=draft.current,seq=sequence.current;sending=true;
   try{
    await api(`/api/rooms/${session.code}/progress`,{method:'POST',body:{text,sequence:seq}});
    if(!stopped){if(seq===sequence.current)dirty.current=false;setSyncError('');}
   }catch(e){
    if(!stopped){
     if(e.status===409){dirty.current=false;setSyncError('The deadline passed before your final update arrived. Results use the last saved text.');}
     else setSyncError('Typing is not syncing. Retrying while the race is active…');
    }
   }finally{sending=false;}
  },120);
  return()=>{stopped=true;clearInterval(timer);};
 },[session.code]);
 const waiting=room?.status==='WAITING';
 const finished=room?.status==='FINISHED';
 const countdown=room?.startsAt>0&&now<room.startsAt;
 const racing=room?.status==='RACING'&&now<room.endsAt;
 const count=countdown?Math.max(1,Math.ceil((room.startsAt-now)/1000)):0;
 const remaining=room?.endsAt?Math.max(0,Math.ceil((room.endsAt-now)/1000)):room?.durationSeconds||60;
 const me=room?.players.find(p=>p.you),friend=room?.players.find(p=>!p.you);
 useEffect(()=>{if(racing)input.current?.focus();},[racing]);
 useEffect(()=>{cursor.current?.scrollIntoView({block:'nearest',behavior:'instant'});},[typed]);
 useEffect(()=>{
  const event=finished?'result':countdown?`count-${count}`:racing?'start':'';
  if(event&&event!==lastSound.current){
   lastSound.current=event;
   playSound(finished?(room.winnerId===user.id?'win':'finish'):countdown?'count':'start');
  }
 },[count,finished,countdown,racing,room?.winnerId,user.id]);
 async function action(path,body){
  if(busy)return;setBusy(true);setError('');unlockSound();
  try{accept(await api(`${base}/${path}`,{method:'POST',body}));}
  catch(e){setError(e.message);}finally{if(alive.current)setBusy(false);}
 }
 async function leave(){
  if(busy)return;
  if(room?.startsAt&&!finished&&!window.confirm('Leave this race? The clock will continue and your saved typing will be scored.'))return;
  setBusy(true);
  try{await api(`${base}/leave`,{method:'POST'});onLeave();}
  catch(e){if([401,403,404].includes(e.status))onLeave();else setError(e.message);}
  finally{if(alive.current)setBusy(false);}
 }
 async function copy(){
  try{await navigator.clipboard.writeText(session.code);setCopied(true);}
  catch{setError('Select and copy the room code manually.');}
 }
 function type(event){
  if(!racing)return;unlockSound();playSound('key');
  const value=event.target.value;setTyped(value);draft.current=value;
  sequence.current++;dirty.current=true;
 }
 return <main className={`content lobby-content enter-page ${waiting?'narrow':''}`}>
  <section className={`panel race-panel ${finished ? 'finish-arena' : ''}`}>
   <div className="room-heading"><span className="eyebrow">{finished?'THE FINISH LINE':'YOUR PRIVATE ARENA'}</span><span className="room-label">ROOM {session.code}</span></div>
   {!room&&<p className="muted">Opening your room…</p>}
   {waiting&&<>
    <h1 className="page-title">Bring on the competition.</h1><p className="muted">Share the code. Your friend readies up. The creator starts.</p>
    <div className="room-code"><strong>{session.code}</strong><button className="secondary" onClick={copy}><Copy size={16}/>{copied?'Copied':'Copy'}</button></div>
    <label className="duration-label">Race duration{me?.host?<select disabled={busy} value={room.durationSeconds} onChange={e=>action('settings',{durationSeconds:Number(e.target.value)})}>
     {[30,60,90,120].map(s=><option key={s} value={s}>{s} seconds</option>)}</select>:<span className="duration-display">{room.durationSeconds} seconds · selected by creator</span>}</label>
    <div className="players">{[0,1].map(i=>{const p=room.players[i];return <div className="player" key={i}><span className="avatar"><UserRound size={20}/></span><div className="player-name"><strong>{p?.name||'Waiting for your friend…'} {p?.you&&<small className="accent">YOU</small>}</strong><small>{p?.host?'Room creator':p?'Challenger':'Share your room code'}</small></div>{p?.host?<Crown size={18} className="accent"/>:p?.ready?<span className="positive ready"><Check size={15}/>Ready</span>:null}</div>;})}</div>
    {me?.host?<button className="primary" disabled={busy||!friend?.ready} onClick={()=>action('start')}>{busy?'Please wait…':friend?.ready?'Start game':'Waiting for friend to be ready'}</button>:
     <button className="primary" disabled={busy||me?.ready} onClick={()=>action('ready')}>{busy?'Please wait…':me?.ready?'Ready — waiting for creator':'I’m ready'}</button>}
    <p className="small muted hint">Changing the duration asks your friend to ready up again.</p>
   </>}
   {countdown&&!finished&&<div className="countdown"><p className="eyebrow">FINGERS ON THE KEYBOARD</p><strong>{count}</strong><p className="muted">{room.durationSeconds} seconds. Make them count.</p></div>}
   {room&&!waiting&&!countdown&&!finished&&<>
    <div className="race-heading"><div><h1 className="page-title">Find your rhythm.</h1><p className="muted small">You vs {friend?.name}</p></div><span className={`clock ${remaining<=10?'negative':''}`}><Timer size={22}/>{remaining}s</span></div>
    <div className="progress-track"><div style={{width:`${Math.min(100,remaining/room.durationSeconds*100)}%`}}/></div>
    <div className="passage" aria-label="Passage to type">{room.paragraph?room.paragraph.split('').map((c,i)=><span key={i} ref={i===typed.length?cursor:null} className={i<typed.length?(typed[i]===c?'correct':'incorrect'):i===typed.length?'caret':''}>{c}</span>):'Preparing your passage…'}</div>
    <label>Type the passage here<textarea ref={input} rows={4} value={typed} onChange={type} onPaste={e=>e.preventDefault()} onDrop={e=>e.preventDefault()} disabled={!racing||!room.paragraph} maxLength={room.paragraph.length} spellCheck={false} autoComplete="off" autoCorrect="off" autoCapitalize="off" placeholder="Start typing…"/></label>
    <p className="muted small">{racing?'Your opponent’s typing stays hidden until the finish.':'Time’s up. Getting your results…'}</p>
   </>}
   {finished&&<RaceResults room={room} user={user} onHome={leave} busy={busy}/>}
   {error&&<p className="error" role="alert">{error}</p>}{syncError&&<p className="error" role="status">{syncError}</p>}
   {!finished&&<button className="text-button" disabled={busy} onClick={leave}>{waiting&&me?.host?'Close room & return home':'Leave room'}</button>}
  </section>
 </main>;
}
