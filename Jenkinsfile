pipeline {
    agent any

    parameters {
        choice(
            name: 'testType',
            choices: ['smoke', 'regression', 'all', 'ui', 'api', 'e2e', 'custom'],
            description: 'Выберите тип тестов для запуска. Для своего тега выберите "custom"'
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

        stage('Run Tests') {
            steps {
                script {
                    if (params.testType == 'custom') {
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
            allure([
                results: [[path: 'build/allure-results']]
            ])
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