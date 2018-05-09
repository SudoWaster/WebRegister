import React, { Component } from 'react';

class Group extends Component {
  constructor() {
    super();
    
    this.state = {
      group: {
        id: 0,
        name: '',
        description: '',
        vacancies: ''
      },
      instructors: [],
      priviledged: false,
      isin: false,
      editable: false
    };
    
    this.join = this.join.bind(this);
    this.leave = this.leave.bind(this);
    this.setValue = this.setValue.bind(this);
  }
  
  componentDidMount() {
    this.props.api.get('group/' + this.props.group, false, true)
    .then((result) => {
      if(result.status === 200) {

        let group = result.data;
        group.id = this.props.group;
        this.setState({ group: group });
      }
    })
    .then(() => {
      this.props.api.get('group/' + this.props.group + '/instructors', false, true)
      .then((result) => {
        if(result.status === 200) {
          this.setState({ instructors: result.data });
        }
      })
      .then(() => {
        this.props.api.get('user', false, true)
        .then((result) => {
          if(result.status === 200) {
            const user = result.data;

            let instructorIds = [];

            for(let instructor of this.state.instructors) {
              instructorIds.push(instructor.id);
            }

            if(user.type === 'ADMIN' || instructorIds.includes(user.id)) {
              this.setState({ priviledged: true });
            }
          }
        });
      });  
    });
    
    this.props.api.get('user/groups', false, true)
      .then((result) => {
      
        if(result.status === 200) {
          const groups = result.data;
          
          let groupIds = [];
          
          for(let group of groups) {
            groupIds.push(group.id);
          }
          
          if(groupIds.includes(this.props.group)) {
            this.setState({ isin: true });
          }
        }
    });
  }
  
  join() {
    this.props.api.put('group/' + this.props.group + '/members', false, true)
    .then((result) => {
      if(result.status === 200) {
        this.componentDidMount();
      }
    })
  }
  
  leave() {
    this.props.api.delete('group/' + this.props.group + '/members', true)
    .then((result) => {
      if(result.status === 200) {
        this.componentDidMount();
      }
    });
  }

  deleteGroup() {
    let deleteConfirmation = window.confirm("Czy na pewno chcesz usunąć grupę " + this.state.group.name + "?");

    if(!deleteConfirmation) {
      return;
    }

    this.props.api.delete('groups/' + this.props.group, true)
    .then((result) => {
      if(result.status === 200) {
        this.props.returnAction();
      }
    })
  }

  setValue(e) {
    let key = e.target.name;
    let value = e.target.value;

    let group = this.state.group;
    group[key] = value;

    this.setState({ group: group });
  }

  save() {
    if(!this.state.priviledged) {
      return;
    }

    this.props.api.put('groups', this.state.group, true);
  }

  render() {
    
    let instructors = this.state.instructors.map((instructor) => (<li key={'instructor-' + this.props.group + '-' + instructor.id}>{instructor.firstname + ' ' + instructor.lastname}</li>));
    
    if(this.state.instructors.length <= 0) {
      instructors = '- brak -';
    }
    
    let addButton = <button onClick={() => {this.leave(); }}>Opuść</button>;

    if(this.state.isin) {
      addButton = (<button onClick={() => { this.join(); }} disabled={this.state.group.vacancies <= 0}>Dołącz</button>);
    }

    let manageButtons = '';
    
    if(this.state.priviledged) {
      manageButtons = (<span><button onClick={() => { this.setState({ editable: !this.state.editable }); }}>Edytuj</button> <button onClick={() => { this.deleteGroup(); }}>Usuń</button></span>);
    }

    let titleElement = (<h2 className="group-title">{this.state.group.name}</h2>);
    let description = this.state.group.description;
    let vacancies = this.state.group.vacancies;
    let saveButton = '';

    if(this.state.editable) {
        titleElement = (<div className="group-title"><input onChange={this.setValue} value={this.state.group.name} name="name" /></div>);
        description = (<textarea onChange={this.setValue} value={this.state.group.description} name="description" />);
        vacancies = (<input type="number" onChange={this.setValue} defaultValue={this.state.group.vacancies} />);
        saveButton = (<button onClick={() => { this.save(); }}>Zapisz</button>);
    }

    return (
      <div>
        <div className="row">
          <div className="col-12">
            {titleElement}
            <div className="group-vacancies">Wolne miejsca: {vacancies}</div>
            {saveButton} {addButton} {manageButtons}
          </div>
        </div>
      
        <div className="row">
          <div className="col-12 group-description">{description}</div>
        </div>
      
        <div className="row group-instructors">
          <div className="col-12 instructors-title">Prowadzący</div>
          <div className="col-12"><ul>{instructors}</ul></div>
        </div>
      </div>
    );
  }
}

export default Group;