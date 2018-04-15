import React, { Component } from 'react';
import './css/User.css';
import Field from './User/Field';

class User extends Component {
  
  constructor() {
    super();
    
    this.state = { 
      user: {
        mail: '',
        firstname: '',
        lastname: '',
        password: '',
        type: 'UNAUTHORIZED',
        id: 0
      },
      groups: [],
      priviledged: false,
      admin: false,
      self: false,
      priviledges: [
        'UNAUTHORIZED',
        'STUDENT',
        'PRIVILEDGED',
        'ADMIN'
      ],
      processing: false
    };
    
    this.setUser = this.setUser.bind(this);
    this.save = this.save.bind(this);
    this.delete = this.delete.bind(this);
  }
  
  componentDidMount() {
    
    let url = 'user';
    
    if(!(this.props.user === undefined || this.props.user === null)) {
      url += '/' + this.props.user;
    }
    
    this.props.api.get(url, false, true)
    .then((result) => {
      if(result.status === 200) {
        let user = result.data;
        user.password = ''
        this.setState({ user: user });
      }
    })
    .then(() => {
      this.props.api.get('user/' + this.state.user.id + '/groups', false, true)
        .then((result) => {
          if(result.status === 200) {
            this.setState({ groups: result.data });
          }
        });
    })
    .then(() => {
      this.props.api.get('user', false, true)
      .then((result) => {
        if(result.status === 200) {
          let self = result.data;
          
          if(self.id === this.state.user.id) {
            this.setState({ priviledged: true });
            this.setState({ self: true });
          }
          
          if(self.type === 'ADMIN') {
            this.setState({ priviledged: true });
            this.setState({ admin: true });
          }
        }
      });
    });
    
  }
  
  setUser(key, value) {
    let user = this.state.user;
    user[key] = value;
    this.setState({ user: user });
  }
  
  save(e) {
    this.setState({ processing: true });
    
    this.props.api.put('user/' + this.state.user.id, { firstname: this.state.user.firstname, lastname: this.state.user.lastname }, true)
    .then((result) => {
      
      this.props.api.put('user/' + this.state.user.id + '/priviledge', { type: this.state.priviledges.indexOf(this.state.user.type) }, true);
      
      if(this.state.self) {
        let password = prompt('Podaj swoje hasło aby kontynuować');
        let newpassword = password;

        if(this.state.user.password !== undefined && this.state.user.password !== null && this.state.user.password !== '') {
          newpassword = this.state.user.password;
        } else {
          if(this.state.admin && this.state.user.password === '') {
            this.setState({ processing: false });
            return;
          }
        }

        this.props.api.put('user/auth', { id: this.state.user.id, oldpassword: password, mail: this.state.user.mail, password: newpassword }, true)
        .then((result) => {
          
          this.setState({ processing: false });
          
          if(result.status >= 400 && result.status <= 403) {
            alert('Złe hasło');
          } else if(result.status === 200 && this.state.self) {
            this.props.api.destroySession();
            this.props.api.auth(this.state.user.mail, newpassword);
          }
        });
      }
    });
    
    e.preventDefault();
  }
  
  delete(e) {
    
    let firstname = this.state.user.firstname;
    let isUserSure = window.confirm('Na pewno chcesz usunąć konto ' + firstname  + '?');
    
    if(isUserSure) {
      let url = 'user';
      if(!this.state.self && this.state.admin) {
        url += '/' + this.state.user.id;
      }
      
      this.props.api.delete(url, true)
      .then((result) => {
        this.props.api.destroySession();
        this.props.onLogout();
      });
      
    }
    
    e.preventDefault();
  }
  
  render() {
    let groups = this.state.groups.map((group) => <li><span className="info-group-progress">{group.progress}%</span> <span className="info-group-name">{group.group.name}</span> </li>);
                                       
    if(this.state.groups.length === 0) {
      groups = <li>brak</li>; 
    }
    
    let additional = '';
    
    if(this.state.self) {
      additional = (<Field name="Nowe hasło" value={this.state.user.password} editable={!this.state.processing} onEdit={(value) => {this.setUser('password', value);}} />);
    }
         
    let button = '';

    if(this.state.priviledged) {
      button = (<span><button onClick={this.save}>Zapisz</button> - <button onClick={this.delete}>Usuń konto</button></span>);
    }
      
    return (
      <div>
        <div className="row">
          <div className="col-6 col-sm-12">
            <Field name="Id" value={this.state.user.id} editable={false} />
            <Field name="Mail" value={this.state.user.mail} editable={this.state.self && !this.state.processing} />
            {additional}
            <Field name="Imię" value={this.state.user.firstname} editable={this.state.priviledged && !this.state.processing} onEdit={(value) => {this.setUser('firstname', value);}}/>
            <Field name="Nazwisko" value={this.state.user.lastname} editable={this.state.priviledged && !this.state.processing} onEdit={(value) => {this.setUser('lastname', value);}} />
            <Field name="Typ" value={this.state.user.type} editable={this.state.admin && !this.state.processing} options={this.state.priviledges} selectable={this.state.admin && !this.state.processing} onEdit={(value) => {this.setUser('type', value);}} />
            {button}
          </div>
          <div className="col-6 col-sm-6">Grupy: 
            <ul className="user-group-list">
              {groups}
            </ul>  
          </div>
        </div>
      </div>
    );
  }
}

export default User;