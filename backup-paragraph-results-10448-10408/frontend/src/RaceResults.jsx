import {useEffect,useRef} from 'react';
import {Trophy,Flag,Handshake,Star,ArrowRight,CheckCircle2,Clock3,Target,Keyboard} from 'lucide-react';

const THEMES = {
  win: {badge:'YOU WIN!', title:'WINNER', caption:'Fast fingers. Well-earned bragging rights.', Icon:Trophy},
  loss: {badge:'ROUND COMPLETE', title:'YOU LOSE', caption:'Good game. Your next win is one race away.', Icon:Flag},
  draw: {badge:'EVENLY MATCHED', title:'IT’S A DRAW', caption:'Same score. Two worthy rivals. Settle it next round.', Icon:Handshake},
};

export default function RaceResults({room,user,onHome,busy}) {
  const me=room.players.find(player=>player.you);
  const opponent=room.players.find(player=>!player.you);
  // Use the authoritative winner ID: rounded WPM can appear equal in a close race.
  const outcome=room.winnerId==null?'draw':room.winnerId===user.id?'win':'loss';
  const {badge,title,caption,Icon}=THEMES[outcome];
  const heading=useRef(null);
  useEffect(()=>{heading.current?.focus({preventScroll:true});},[]);
  if(!me || !opponent)return <p className="muted">Preparing the final scores…</p>;

  return <div className={`finish-screen finish-${outcome}`}>
    <div className="finish-hero">
      <div className="finish-orbit" aria-hidden="true"><div className="finish-medal"><Icon strokeWidth={1.6}/></div></div>
      <p className="finish-badge">{badge}</p>
      <h1 ref={heading} tabIndex={-1} className="finish-title">{title}</h1>
      <p className="finish-caption">{caption}</p>
      <div className="finish-stars" aria-hidden="true"><Star/><Star/><Star/></div>
    </div>

    <section className="finish-scoreboard" aria-label="Your race statistics">
      <div className="finish-speed"><span className="finish-label">YOUR TYPING SPEED</span>
        <div><strong>{me.wpm}</strong><span>WPM</span></div>
        <span className="finish-speed-note">Correct characters · full race duration</span>
      </div>
      <div className="finish-metrics">
        <div><Target size={17}/><span>Accuracy</span><strong>{me.accuracy}%</strong></div>
        <div><Keyboard size={17}/><span>Mistakes</span><strong>{me.mistakes}</strong></div>
        <div><Clock3 size={17}/><span>Duration</span><strong>{room.durationSeconds}<small>s</small></strong></div>
      </div>
    </section>

    <div className="finish-match-heading"><span>THE MATCHUP</span><span>{outcome==='draw'?'Honours shared':room.winner}</span></div>
    <div className="finish-matchup">
      {[me,opponent].map((player,index)=>{
        const won=outcome!=='draw' && (player.you?outcome==='win':outcome==='loss');
        const state=outcome==='draw'?'draw':won?'win':'loss';
        return <article className={`finish-player player-${state}`} key={index}>
          <div className="finish-player-top"><span className="finish-player-outcome">{won?<Trophy size={13}/>:null}{state==='draw'?'DRAW':won?'WINNER':'LOST'}</span><span className="finish-identity">{player.you?'YOU':'OPPONENT'}</span></div>
          <h2>{player.name}</h2>
          <p className="finish-player-wpm"><strong>{player.wpm}</strong> WPM</p>
          <p className="finish-player-detail">{player.accuracy}% accuracy <span aria-hidden="true">·</span> {player.mistakes} mistakes</p>
        </article>;
      })}
    </div>

    <p className="finish-save" role="status">{room.resultSaved?<><CheckCircle2 size={14}/>Saved to your match history</>:<>Saving your result…</>}</p>
    <button type="button" className="finish-home" disabled={busy} onClick={onHome}>{busy?'Please wait…':'BACK TO HOME'}<ArrowRight size={17}/></button>
    <p className="finish-next">Create another room or check your profile history.</p>
    <details className="finish-scoring"><summary>How the score works</summary><p>WPM = correct characters ÷ 5 ÷ race minutes. Mistakes count incorrect characters remaining in the saved text. Corrected mistakes do not count. The server decides the winner using the exact correct-character count, before rounding WPM.</p></details>
  </div>;
}
