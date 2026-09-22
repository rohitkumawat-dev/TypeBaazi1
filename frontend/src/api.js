let csrf;
export async function refreshCsrf() {
 const response=await fetch('/api/auth/csrf',{credentials:'same-origin'});
 if(!response.ok) throw new Error('Could not connect. Check that the Java server is running.');
 csrf=await response.json();
}
export async function api(path,{method='GET',body,signal}={}) {
 const headers={};
 if(method!=='GET') {
  if(!csrf) await refreshCsrf();
  headers[csrf.headerName]=csrf.token;
 }
 if(body!==undefined) headers['Content-Type']='application/json';
 let response;
 try {
  response=await fetch(path,{method,headers,credentials:'same-origin',
   body:body===undefined?undefined:JSON.stringify(body),signal});
 } catch(err) {
  if(err.name==='AbortError') throw err;
  throw new Error('Connection lost. Check your internet and the Java server.');
 }
 if(!response.ok) {
  const data=await response.json().catch(()=>({}));
  const error=new Error(data.message || data.detail ||
   (response.status===401?'Please log in again.':response.status===403?
    'Session changed or access denied. Refresh the page and try again.':'Request failed. Please try again.'));
  error.status=response.status;
  if(response.status===403) csrf=undefined;
  throw error;
 }
 return response.status===204?null:response.json();
}
