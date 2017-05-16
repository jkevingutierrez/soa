import gulp from 'gulp';
import browserSync from 'browser-sync';
import historyApiFallback from 'connect-history-api-fallback';

gulp.task('server', () => {
    return browserSync.create().init({
        files: ['./**/*.css', './**/*.js', './**/*.html'],
        startPath: '/',
        watchOptions: {
            ignoreInitial: true,
            ignored: ['./node_modules']
        },
        server: {
            baseDir: ['.'],
            middleware: [historyApiFallback()]
        }
    });
});

gulp.task('default', ['server']);
