// TODO : implement "create component" prompt
module.exports = {

    component: {
        options: {
            questions: [
                {
                    config: 'component.name', // arbitrary name or config for any other grunt task
                    type: 'input', // list, checkbox, confirm, input, password
                    message: 'Name your component'//, // Question to ask the user, function needs to return a string,
                    //validate: function (value) {
                    //    console.log('validate ', value);
                    //    return value === 'next';
                    //} // return true if valid, error message if invalid. works only with type:input
                    //filter: function (value) {
                    //    console.log('value', value);
                    //}, // modify the answer
                    //when: function (answers) {
                    //    console.log('answers', answers);
                    //}// only ask this question when this function returns true
                },
                {
                    config: 'component.js', // arbitrary name or config for any other grunt task
                    type: 'list', // list, checkbox, confirm, input, password
                    message: 'Use JavaScript?',//, // Question to ask the user, function needs to return a string,
                    choices: [
                        { name: 'no', checked: true },
                        { name: 'yes'}
                    ]
                    //validate: function (value) {
                    //    console.log('validate ', value);
                    //    return value === 'next';
                    //} // return true if valid, error message if invalid. works only with type:input
                    //filter: function (value) {
                    //    console.log('value', value);
                    //}, // modify the answer
                    //when: function (answers) {
                    //    console.log('answers', answers);
                    //}// only ask this question when this function returns true
                }
            ],
            then: function(results, done) {
                //someAsyncFunction(function () {
                //});
                    console.log('results', results);
                    done();
                return true;
            }
            }
    }

};
