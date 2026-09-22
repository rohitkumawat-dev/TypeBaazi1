let context;
let muted=localStorage.getItem('typebaazi-muted')==='true';
export function isMuted(){return muted;}
export function setMuted(value){muted=value;localStorage.setItem('typebaazi-muted',String(value));}
export function unlockSound(){
 try {
  if(!context) context=new (window.AudioContext||window.webkitAudioContext)();
  if(context.state==='suspended') context.resume().catch(()=>{});
 } catch { /* Browsers without Web Audio can still play the game. */ }
}
function tone(hz,at,length=0.1,volume=0.045){
 if(muted || !context || context.state!=='running')return;
 const oscillator=context.createOscillator(),gain=context.createGain();
 const time=context.currentTime+at;
 oscillator.type='sine';oscillator.frequency.value=hz;
 gain.gain.setValueAtTime(0,time);
 gain.gain.linearRampToValueAtTime(volume,time+0.008);
 gain.gain.exponentialRampToValueAtTime(0.001,time+length);
 oscillator.connect(gain);gain.connect(context.destination);
 oscillator.start(time);oscillator.stop(time+length+0.01);
 oscillator.onended=()=>{oscillator.disconnect();gain.disconnect();};
}
export function playSound(kind){
 if(kind==='key')tone(380,0,0.025,0.009);
 if(kind==='count')tone(520,0);
 if(kind==='start'){tone(660,0);tone(880,0.12,0.2);}
 if(kind==='finish'){tone(440,0);tone(330,0.16,0.2);}
 if(kind==='win'){tone(523,0);tone(659,0.13);tone(784,0.26,0.3);}
}
