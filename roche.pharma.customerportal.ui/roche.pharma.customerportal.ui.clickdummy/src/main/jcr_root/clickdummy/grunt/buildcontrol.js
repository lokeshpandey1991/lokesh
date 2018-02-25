module.exports = {

    app: {
        options: {
            dir: '<%= config.app %>',
            remote: 'ssh://git@tools.sapient.com/audiiph/frontend-releases.git',
            branch: 'releases',
            tag: 'v<%= package.version %>',
            commit: true,
            push: true,
            message: 'Built %sourceName% from commit %sourceCommit% on branch %sourceBranch%'
        }
    }

};
