pipeline {
    agent any

    parameters {
        choice(
            name: 'testType',
            choices: ['smoke', 'regression', 'all', 'ui', 'api', 'e2e', 'custom', 'rerun-failed'],
            description: 'Выберите тип тестов для запуска. Для своего тега выберите "custom". Для перезапуска упавших — "rerun-failed"'
        )
        string(
            name: 'customTag',
            defaultValue: '',
            description: 'Кастомный тег (используется если testType = custom). Например: SMOKE, UI, TS001'
        )
        string(
            name: 'branch',
            defaultValue: 'main',
            description: 'Ветка для сборки и запуска тестов'
        )
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: "${params.branch}",
                    url: 'https://github.com/MiraidaOrozbaeva/Apple.git'
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean assemble'
            }
        }

        stage('Restore Failed Test List') {
            when {
                expression { params.testType == 'rerun-failed' }
            }
            steps {
                script {
                    // Ищем предыдущий билд у которого есть артефакт failed-tests.txt
                    def targetBuild = currentBuild.previousBuild
                    while (targetBuild != null) {
                        def artifactExists = targetBuild.artifacts.find { it.fileName == 'failed-tests.txt' }
                        if (artifactExists) break
                        targetBuild = targetBuild.previousBuild
                    }

                    if (!targetBuild) {
                        error("❌ Не найден билд с артефактом failed-tests.txt. Сначала запустите любой тип тестов.")
                    }

                    echo "Беру список упавших тестов из билда #${targetBuild.number}"

                    // Читаем артефакт через Jenkins API без curl и без проблем с авторизацией
                    def jobName = env.JOB_NAME.replace('/', '/job/')
                    sh """
                        curl -s --user admin:\$(cat /var/jenkins_home/secrets/initialAdminPassword 2>/dev/null || echo '') \
                            -o failed-tests.txt \
                            "http://localhost:8080/job/${jobName}/${targetBuild.number}/artifact/failed-tests.txt" \
                            || true
                    """

                    // Если curl не сработал — пробуем через workspace предыдущего билда
                    def failedTests = ''
                    try {
                        failedTests = readFile('failed-tests.txt').trim()
                    } catch (e) {
                        failedTests = ''
                    }

                    if (!failedTests) {
                        error("❌ Файл failed-tests.txt пуст — нет упавших тестов для перезапуска.")
                    }

                    echo "Найдены упавшие тесты (${failedTests.split('\n').size()} шт.):\n${failedTests}"
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    if (params.testType == 'rerun-failed') {
                        def failedTests = readFile('failed-tests.txt').trim()
                        // Формат classname#methodname -> --tests "classname.methodname"
                        def testArgs = failedTests.split('\n')
                            .findAll { it.trim() }
                            .collect { line ->
                                def trimmed = line.trim().replace('#', '.')
                                "--tests \"${trimmed}\""
                            }.join(' ')

                        echo "Перезапускаем упавшие тесты: ${testArgs}"
                        // Используем rerunFailedTests задачу чтобы сохранить jvmArgs
                        sh "./gradlew rerunFailedTests ${testArgs}"

                    } else if (params.testType == 'custom') {
                        if (!params.customTag?.trim()) {
                            error("Поле customTag не может быть пустым если testType = custom")
                        }
                        // Приводим тег к верхнему регистру т.к. в build.gradle.kts теги в uppercase
                        def tag = params.customTag.trim().toUpperCase()
                        echo "Запускаем кастомный тег: ${tag} на ветке: ${params.branch}"
                        sh "./gradlew test -Dgroups=${tag}"

                    } else {
                        def gradleTask = resolveGradleTask(params.testType)
                        echo "Запускаем задачу: ${gradleTask} на ветке: ${params.branch}"
                        sh "./gradlew ${gradleTask}"
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                try {
                    def failedList = []

                    def xmlOutput = sh(
                        script: "find build/test-results -name '*.xml' 2>/dev/null || true",
                        returnStdout: true
                    ).trim()

                    if (xmlOutput) {
                        xmlOutput.split('\n').each { xmlPath ->
                            def trimmedPath = xmlPath.trim()
                            if (trimmedPath) {
                                def xml = readFile(trimmedPath)
                                def parsed = new XmlSlurper().parseText(xml)
                                parsed.'testcase'.each { tc ->
                                    if (tc.'failure'.size() > 0 || tc.'error'.size() > 0) {
                                        failedList << "${tc.@classname}#${tc.@name}"
                                    }
                                }
                            }
                        }
                    }

                    writeFile(
                        file: 'failed-tests.txt',
                        text: failedList.join('\n')
                    )

                    if (failedList) {
                        echo "⚠️ Упавшие тесты сохранены (${failedList.size()} шт.):\n${failedList.join('\n')}"
                    } else {
                        echo "✅ Упавших тестов нет."
                    }

                    archiveArtifacts artifacts: 'failed-tests.txt', allowEmptyArchive: true

                } catch (err) {
                    echo "⚠️ Не удалось собрать список упавших тестов: ${err.message}"
                }
            }

            allure(
                includeProperties: false,
                jdk: '',
                results: [[path: 'build/allure-results']],
                report: 'allure-report',
                reportBuildPolicy: 'ALWAYS'
            )
        }

        success {
            echo "✅ Тесты [${params.testType}] на ветке [${params.branch}] прошли успешно!"
        }
        failure {
            echo "❌ Тесты [${params.testType}] на ветке [${params.branch}] завершились с ошибкой."
        }
    }
}

def resolveGradleTask(String testType) {
    switch (testType) {
        case 'smoke':      return 'smokeTests'
        case 'regression': return 'regressionTests'
        case 'ui':         return 'uiTests'
        case 'api':        return 'apiTests'
        case 'e2e':        return 'e2eTests'
        case 'all':        return 'allTests'
        default:
            error("Неизвестный тип тестов: ${testType}")
    }
}