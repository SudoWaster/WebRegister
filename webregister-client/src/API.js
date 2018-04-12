class API {
  constructor(url) {
    this.apiUrl = url;
  }
  
  auth(mail, password) {
    return this.request('auth', 'post', { mail: mail, password: password }, false)
    .then((result) => {
      
      if(result.status === 200) {
        this.token = result.data.token;
        return true;
      }
      
      return false;
    })
  }
  
  isAuthenticated() {
    return this.token !== undefined && this.token !== null;
  }
  
  destroySession() {
    this.token = undefined;
  }
  
  get(url, data = false, auth = false) {
    let params = '';
    
    if(data) {
      for(let p in data) {
        if(params.includes('?')) {
          params += '&';
        } else {
          params += '?';
        }

        params += p + '=' + data[p];
      }
    }
    
    return this.request(url + params, 'get', {}, auth);
  }
  
  post(url, data = {}, auth = false) {
    return this.request(url, 'post', data, auth);
  }
  
  put(url, data = {}, auth = false) {
    return this.request(url, 'put', data, auth);
  }
  
  delete(url, auth = false) {
    return this.request(url, 'delete', {}, auth);
  }
  
  request(url, method = 'get', data = {}, auth = false) {
    
    let fetchData = {
      method: method,
      headers: {
        'content-type': 'application/x-www-form-urlencoded'
      },
    };
    
    if (method.toUpperCase() === 'POST' || method.toUpperCase() === 'PUT') {
      
      let body = new URLSearchParams();

      if(data) {
        for(const k in data) {
          body.append(k, data[k]);
        }

        fetchData.body = body;
      }
    }
    
    let fetchUrl = this.apiUrl + url; 
    
    if(auth) {
      if(fetchUrl.includes('?')) {
        fetchUrl += '&';
      } else {
        fetchUrl += '?';
      }
      
      fetchUrl += 'token=' + this.token;
    }
    
    return fetch(fetchUrl, fetchData)
      .then(
        (data) => { 
          let status = data.status;
          
          let result = { status: status };
          
          if(status >= 200 && status < 300) {
            return data.json().then((json) => { result.data = json; return result; });
          } else {
            if (status === 401) {
              this.token = undefined;
            }
            
            return result;
          }
        })
    .catch(
      (err) => {
        console.log(err);
        
        return { status: '503' };
      });
  } 
}

export default API;