var CalloutBox = React.createClass({
	getInitialState: function() {
		return {data: []};
	},
	componentDidMount: function() {
	    this.loadCalloutsFromServer();
	    setInterval(this.loadCalloutsFromServer, 1000);
	},
    loadCalloutsFromServer: function() {
	    $.ajax({
	        url: this.props.url,
	        dataType: 'json',
            cache: false,
            success: function(data) {
                this.setState({data: data});
            }.bind(this),
            error: function(xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    },	
    render: function() {
      return (
        <div className="calloutBox">
            <h2>Callout Requests</h2>
            <CalloutList data={this.state.data} />
        </div>
      );
    }
});

var CalloutList = React.createClass({
    render: function() {
        var calloutItems = this.props.data
            .filter(function(item){ return item;})
            .map(function(item, idx){
                if (item) {
                    return (
                        <CalloutItem 
                            key={idx}
                            index={idx}
                            address={item.address}
                            timestamp={(new Date(item.timestamp)).toISOString()}
                            urlParams={item.urlParams}
                            content={item.content}
                            headers={item.headers}
                            parsedParams={item.parsedParams}
                        />
                    );
                }
            });
        return (
            <div className="calloutList">
                {calloutItems}
            </div>
        );
    }
});

var CalloutItem = React.createClass({
    parseList: function(obj) {
        var list = (<ul></ul>);
        if (obj) {
            var lis = [];
            for (var param in obj){
                lis.push({name: param, value:obj[param].toString()});
            };
            list = (
                    <ul>
                        {lis.map(function(li, idx){
                            return (
                                <li key={idx}>{li.name} --> {li.value}</li>    
                            );
                        })}
                    </ul>
            );
        }
        return list;
    },
    render: function() {
        var urlParams = this.parseList(this.props.urlParams);
        var headers = this.parseList(this.props.headers);
        var parsedParams = this.parseList(this.props.parsedParams);
        return (
            <div className="calloutItem">
                <h4 className="calledOn">#{this.props.index + 1} Request at: {this.props.timestamp}</h4>
                <h4 className="callerAddress">Caller Address:</h4>
                {this.props.address}
                <h4 className="urlParams">URL Params</h4>
                { urlParams }
                <h4 className="headers">Headers</h4>
                {headers}
                <h4 className="rawContent">Raw Content</h4>
                {this.props.content}
                <h4 className="parsedParams">Parameters</h4>
                {parsedParams}
            </div>
        );
    }
});

ReactDOM.render(
  <CalloutBox url="/json" />,
  document.getElementById('content')
);