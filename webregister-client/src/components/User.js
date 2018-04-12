import React, { Component } from 'react';

class User extends Component {
  
  constructor() {
    super();
    
    this.state = { user: {
      mail: '',
      firstname: '',
      lastname: '',
      type: 'UNAUTHORIZED',
      id: 0
    },
    groups: []};
  }
  
  componentDidMount() {
    let url = 'user';
    
    if(!(this.props.user === undefined || this.props.user === null)) {
      url += '/' + this.props.user;
    }
    
    this.props.api.get(url, false, true)
    .then((result) => {
      if(result.status === 200) {
        this.setState({ user: result.data });
      }
    })
    .then(() => {
      this.props.api.get('user/' + this.state.user.id + '/groups', false, true)
        .then((result) => {
          if(result.status === 200) {
            this.setState({ groups: result.data });
          }
        });
    });
    
  }
  
  render() {
    let groups = this.state.groups.map((group) => <li>{group.group.name} - {group.progress}%</li>);
                                       
    if(this.state.groups.length === 0) {
      groups = <li>brak</li>; 
    }
                                       
    return (
      <div>
        <ul>
          <li>Id: {this.state.user.id}</li>
          <li>Mail: {this.state.user.mail}</li>
          <li>Imię: {this.state.user.firstname}</li>
          <li>Nazwisko: {this.state.user.lastname}</li>
          <li>Typ: {this.state.user.type}</li>
          <li>Grupy: 
            <ul>
              {groups}
            </ul>  
          </li>
        </ul>
        
        
      </div>
    );
  }
}

export default User;