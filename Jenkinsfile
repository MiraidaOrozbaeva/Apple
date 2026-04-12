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
            description: 'Кастомный тег (используется если testType = custom). Например: TS001'
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
                    def prevBuild = currentBuild.previousBuild
                    if (!prevBuild) {
                        error("❌ Нет предыдущего билда для получения списка упавших тестов.")
                    }

                    def jobName = env.JOB_NAME
                    def prevBuildNum = prevBuild.number

                    sh """
                        curl -s -o failed-tests.txt \
                          http://localhost:8080/job/${jobName}/${prevBuildNum}/artifact/failed-tests.txt
                    """

                    def failedTests = readFile('failed-tests.txt').trim()
                    if (!failedTests) {
                        error("❌ Файл failed-tests.txt пуст — нет упавших тестов для перезапуска.")
                    }
                    echo "Найдены упавшие тесты:\n${failedTests}"
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    if (params.testType == 'rerun-failed') {
                        def failedTests = readFile('failed-tests.txt').trim()
                        def testArgs = failedTests.split('\n').collect { line ->
                            def trimmed = line.trim().replace('#', '.')
                            "--tests \"${trimmed}\""
                        }.join(' ')
                        echo "Перезапускаем упавшие тесты: ${testArgs}"
                        sh "./gradlew test ${testArgs}"

                    } else if (params.testType == 'custom') {
                        if (!params.customTag?.trim()) {
                            error("Поле customTag не может быть пустым если testType = custom")
                        }
                        echo "Запускаем кастомный тег: ${params.customTag} на ветке: ${params.branch}"
                        sh "./gradlew test -Dgroups=${params.customTag}"

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
                            def xml = readFile(xmlPath.trim())
                            def parsed = new XmlSlurper().parseText(xml)
                            parsed.'testcase'.each { tc ->
                                if (tc.'failure'.size() > 0 || tc.'error'.size() > 0) {
                                    failedList << "${tc.@classname}#${tc.@name}"
                                }
                            }
                        }
                    }

                    writeFile(
                        file: 'failed-tests.txt',
                        text: failedList.join('\n')
                    )

                    echo failedList
                        ? "⚠️ Упавшие тесты сохранены (${failedList.size()} шт.): ${failedList.join(', ')}"
                        : "✅ Упавших тестов нет."

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